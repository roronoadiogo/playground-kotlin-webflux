package com.roronoadiogo.playground.webflux.domain.port.out

import com.roronoadiogo.playground.webflux.domain.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductRepository {

    fun findById(id: Long): Mono<Product>
    fun findAll(): Flux<Product>
    fun save(product: Product): Mono<Product>
    fun deleteById(id: Long): Mono<Void>

}