package com.roronoadiogo.playground.webflux.service

import com.roronoadiogo.playground.webflux.domain.model.Product
import com.roronoadiogo.playground.webflux.domain.port.`in`.ProductUseCase
import com.roronoadiogo.playground.webflux.domain.port.out.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(private val productRepository: ProductRepository): ProductUseCase{
    override fun createProduct(product: Product): Mono<Product> {
        return productRepository.save(product)
    }

    override fun getProductById(id: Long): Mono<Product> {
        return productRepository.findById(id)
    }

    override fun getAllProducts(): Flux<Product> {
        return productRepository.findAll()
    }

    override fun deleteProductBytId(id: Long): Mono<Void> {
        return productRepository.deleteById(id)
    }
}