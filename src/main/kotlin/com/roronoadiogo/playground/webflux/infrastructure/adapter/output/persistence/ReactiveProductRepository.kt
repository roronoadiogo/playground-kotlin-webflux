package com.roronoadiogo.playground.webflux.infrastructure.adapter.output.persistence

import com.roronoadiogo.playground.webflux.domain.exception.DatabaseOperationException
import com.roronoadiogo.playground.webflux.domain.exception.ProductNotFoundException
import com.roronoadiogo.playground.webflux.domain.model.Product
import com.roronoadiogo.playground.webflux.domain.port.out.ProductRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Repository
class ReactiveProductRepository(private val reactiveCrudRepository: DatabaseClient) : ProductRepository {

    override fun findById(id: Long): Mono<Product> {
        return reactiveCrudRepository.sql("SELECT * FROM products WHERE id = :id")
            .bind("id", id)
            .map { row, _ ->
                Product(
                    id = row.get("id") as Long,
                    name = row.get("name") as String,
                    price = row.get("price").toBigDecimalOrZero()
                )
            }
            .one()
            .switchIfEmpty(Mono.error(ProductNotFoundException("Product with id $id not found")))
            .onErrorResume { e ->
                Mono.error(DatabaseOperationException("Error fetching product with id $id", e))
            }
    }

    override fun findAll(): Flux<Product> {
        return reactiveCrudRepository.sql("SELECT * FROM products")
            .map { row, _ ->
                val id = row.get("id") as Long
                val name = row.get("name") as String
                val price = row.get("price").toBigDecimalOrZero()
                Product(id = id, name = name, price = price)
            }
            .all()
            .onErrorResume { e ->
                Flux.error(DatabaseOperationException("Error fetching all products", e))
            }
    }

    override fun save(product: Product): Mono<Product> {
        return reactiveCrudRepository.sql("INSERT INTO products (name, price) VALUES (:name, :price) RETURNING id")
            .bind("name", product.name)
            .bind("price", product.price)
            .map { row, _ -> product.copy(id = row.get("id") as Long) }
            .one()
            .onErrorResume { e ->
                Mono.error(DatabaseOperationException("Error saving product", e))
            }
    }

    override fun deleteById(id: Long): Mono<Void> {
        return reactiveCrudRepository.sql("DELETE FROM products WHERE id = :id")
            .bind("id", id)
            .then()
            .onErrorResume { e ->
                Mono.error(DatabaseOperationException("Error deleting product with id $id", e))
            }
    }

    private fun Any?.toBigDecimalOrZero(): BigDecimal {
        return when (this) {
            is BigDecimal -> this
            is Long -> BigDecimal.valueOf(this)
            is Int -> BigDecimal.valueOf(this.toLong())
            is Double -> BigDecimal.valueOf(this)
            is Float -> BigDecimal.valueOf(this.toDouble())
            else -> BigDecimal.ZERO
        }
    }
}