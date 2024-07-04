package com.roronoadiogo.playground.webflux.infrastructure.adapter.input.reactive;

import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.request.ProductRequestDTO;
import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.response.ProductResponseDTO;
import com.roronoadiogo.playground.webflux.infrastructure.adapter.mapper.ProductMapper;
import com.roronoadiogo.playground.webflux.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI

@RestController
@RequestMapping("/api/reactive/products")
class ProductReactiveController(
        private val productService: ProductService,
        private val productMapper: ProductMapper) {

    @GetMapping
    fun getAllProducts(): Flux<ProductResponseDTO> {
        return productService.getAllProducts()
            .map { productMapper.toDTO(it) }
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): Mono<ResponseEntity<ProductResponseDTO>> {
        return productService.getProductById(id)
            .map { product -> ResponseEntity.ok(productMapper.toDTO(product)) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun createProduct(@RequestBody productRequestDTO: ProductRequestDTO): Mono<ResponseEntity<ProductResponseDTO>> {
        val productEntity = productMapper.toEntity(productRequestDTO)
        return productService.createProduct(productEntity)
            .map { createdProduct ->
                val uri = URI.create("/api/common/products/${createdProduct.id}")
                ResponseEntity.created(uri).body(productMapper.toDTO(createdProduct))
            }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): Mono<ResponseEntity<Void>> {
        return productService.deleteProductById(id)
            .map { ResponseEntity.noContent().build<Void>() }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

}

