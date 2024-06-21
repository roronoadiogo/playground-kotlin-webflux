package com.roronoadiogo.playground.webflux.infrastructure.adapter.input.common

import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.request.ProductRequestDTO
import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.response.ProductResponseDTO
import com.roronoadiogo.playground.webflux.infrastructure.adapter.mapper.ProductMapper
import com.roronoadiogo.playground.webflux.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/common/products")
class ProductCommonController(
    private val productService: ProductService,
    private val productMapper: ProductMapper) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductResponseDTO>> {
        val products = productService.getAllProducts().collectList().block()
        return ok(products?.map { productMapper.toDTO(it) } ?: emptyList())
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponseDTO> {
        val product = productService.getProductById(id).block()
        return if (product != null) ok(productMapper.toDTO(product)) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createProduct(@RequestBody productRequestDTO: ProductRequestDTO): ResponseEntity<ProductResponseDTO> {
        val productEntity = productMapper.toEntity(productRequestDTO)
        val createdProduct = productService.createProduct(productEntity).blockOptional()
        return createdProduct.map { created -> ok(productMapper.toDTO(created)) }
            .orElseGet { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProductBytId(id).block()
        return ResponseEntity.noContent().build()
    }
}