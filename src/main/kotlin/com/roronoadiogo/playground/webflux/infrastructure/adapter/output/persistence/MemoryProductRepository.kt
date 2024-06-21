package com.roronoadiogo.playground.webflux.infrastructure.adapter.output.persistence

import com.roronoadiogo.playground.webflux.domain.model.Product
import com.roronoadiogo.playground.webflux.domain.port.out.ProductRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MemoryProductRepository(private val reactiveCrudRepository: DatabaseClient) : ProductRepository {
    override fun findById(id: Long): Mono<Product> {
        try {
            return reactiveCrudRepository.sql("SELECT * FROM products WHERE id = :id")
                .bind("id", id)
                .map { row, _ -> Product(id = row.get("id", Long::class.java)!!,
                    name = row.get("name", String::class.java)!!,
                    price = row.get("price", java.math.BigDecimal::class.java)!!)}.one()
        }catch (e:NullPointerException){
            throw e
        }
    }

    override fun findAll(): Flux<Product> {
        try {
             return reactiveCrudRepository.sql("SELECT * FROM products")
                .map { row, _ -> Product(id = row.get("id", Long::class.java)!!,
                    name = row.get("name", String::class.java)!!,
                    price = row.get("price", java.math.BigDecimal::class.java)!!)}.all()
        }catch (e:NullPointerException){
            throw e
        }
    }

    override fun save(product: Product): Mono<Product> {
        try {
            return reactiveCrudRepository.sql("INSERT INTO products (name, price) VALUES (:name, :price) RETURNING id")
                .bind("name", product.name)
                .bind("price", product.price)
                .map { row, _ ->product.copy(id = row.get("id", Long::class.java)!!)}.one()
        }catch (e:NullPointerException){
            throw e
        }
    }

    override fun deleteById(id: Long): Mono<Void> {
            return reactiveCrudRepository.sql("DELETE FROM products WHERE id = :id")
                .bind("id", id)
                .then()
    }
}