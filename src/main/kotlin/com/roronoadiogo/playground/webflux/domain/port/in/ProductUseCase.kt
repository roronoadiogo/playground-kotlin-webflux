package com.roronoadiogo.playground.webflux.domain.port.`in`

import com.roronoadiogo.playground.webflux.domain.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductUseCase {

    fun createProduct(product: Product): Mono<Product>
    fun getProductById(id: Long): Mono<Product>
    fun getAllProducts(): Flux<Product>
    fun deleteProductBytId(id: Long): Mono<Void>

}