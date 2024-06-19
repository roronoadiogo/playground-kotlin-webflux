package com.roronoadiogo.playground.webflux.domain.model

import java.math.BigDecimal

data class Product(val id: Long, val name: String, val price: BigDecimal)