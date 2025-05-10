package com.example.ezbill.models

data class Order(
    val productNames: List<String> = listOf(),
    val totalPrice: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val buyerEmail: String = ""
)
