package com.roronoadiogo.playground.webflux.infrastructure.adapter.input.reactive

import com.roronoadiogo.playground.webflux.domain.exception.DatabaseOperationException
import com.roronoadiogo.playground.webflux.domain.exception.ProductNotFoundException
import com.roronoadiogo.playground.webflux.domain.port.`in`.ProductUseCase
import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.request.ProductRequestDTO
import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.response.ProductResponseDTO
import com.roronoadiogo.playground.webflux.infrastructure.adapter.mapper.ProductMapper
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

@RestController
@RequestMapping("/api/reactive/products")
class ProductReactiveController(
    private val productService: ProductUseCase,
    private val productMapper: ProductMapper
) {

    @GetMapping
    @Operation(summary = "Get all products", description = "Return all products")
    fun getAllProducts(): Flux<ProductResponseDTO> {
        return productService.getAllProducts()
            .map { productMapper.toDTO(it) }
            .onErrorResume { e ->
                Flux.error(DatabaseOperationException("Error fetching all products", e))
            }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Return a product by its ID")
    fun getProductById(@PathVariable id: Long): Mono<ResponseEntity<ProductResponseDTO>> {
        return productService.getProductById(id)
            .map { product -> ResponseEntity.ok(productMapper.toDTO(product)) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume { e ->
                if (e is ProductNotFoundException) {
                    Mono.just(ResponseEntity.notFound().build())
                } else {
                    Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                }
            }
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product")
    fun createProduct(@RequestBody productRequestDTO: ProductRequestDTO): Mono<ResponseEntity<ProductResponseDTO>> {
        val productEntity = productMapper.toEntity(productRequestDTO)
        return productService.createProduct(productEntity)
            .map { created ->
                val uri = URI.create("/api/reactive/products/${created.id}")
                ResponseEntity.created(uri).body(productMapper.toDTO(created))
            }
            .onErrorResume { e ->
                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
            }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID", description = "Delete a product by its ID")
    fun deleteProduct(@PathVariable id: Long): Mono<ResponseEntity<Void>> {
        return productService.deleteProductById(id)
            .map { ResponseEntity.noContent().build<Void>() }
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume { e ->
                if (e is ProductNotFoundException) {
                    Mono.just(ResponseEntity.notFound().build())
                } else {
                    Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                }
            }
    }
}

