package com.roronoadiogo.playground.webflux.domain.port.`in`

import com.roronoadiogo.playground.webflux.domain.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Interface for product use case operations.
 */
interface ProductUseCase {

    /**
     * Creates a new product.
     *
     * @param product The product to be created.
     * @return The created product.
     * @throws ProductCreationException If an error occurs while creating the product.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun createProduct(product: Product): Mono<Product>

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to be retrieved.
     * @return The retrieved product, or an error if the product is not found.
     * @throws ProductNotFoundException If the product with the specified ID is not found.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun getProductById(id: Long): Mono<Product>

    /**
     * Retrieves all products.
     *
     * @return A flux containing all retrieved products.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun getAllProducts(): Flux<Product>

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to be deleted.
     * @return A Mono<Void> indicating the completion of the operation.
     * @throws ProductNotFoundException If the product with the specified ID is not found.
     * @throws DatabaseOperationException If an error occurs while accessing the database.
     */
    fun deleteProductById(id: Long): Mono<Void>
}