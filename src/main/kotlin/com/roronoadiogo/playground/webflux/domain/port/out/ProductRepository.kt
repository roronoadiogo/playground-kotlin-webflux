package com.roronoadiogo.playground.webflux.domain.port.out

import com.roronoadiogo.playground.webflux.domain.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Interface for product persistence operations.
 */

interface ProductRepository {

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to be found.
     * @return The found product, or an error if the product is not found.
     * @throws ProductNotFoundException If the product with the specified ID is not found.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun findById(id: Long): Mono<Product>

    /**
     * Finds all products.
     *
     * @return All found products.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun findAll(): Flux<Product>

    /**
     * Saves a product.
     *
     * @param product The product to be saved.
     * @return The saved product.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun save(product: Product): Mono<Product>

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to be deleted.
     * @return A Mono<Void> indicating the completion of the operation.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun deleteById(id: Long): Mono<Void>
}