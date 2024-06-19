package com.roronoadiogo.playground.webflux.infrastructure.adapter.input.dto.response

import java.math.BigDecimal

data class ProductResponseDTO(val id: Long, val name: String, val price: BigDecimal)

