package com.roronoadiogo.playground.webflux.infrastructure.adapter.mapper

import com.roronoadiogo.playground.webflux.domain.model.Product
import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.request.ProductRequestDTO
import com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.response.ProductResponseDTO
import org.mapstruct.Mapper

@Mapper
interface ProductMapper {

    fun toDTO(product: Product):ProductResponseDTO
    fun toEntity(productRequestDTO: ProductRequestDTO):Product
}