package com.roronoadiogo.playground.webflux.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("products")
data class Product(@Id val id: Long, val name: String, val price: BigDecimal)