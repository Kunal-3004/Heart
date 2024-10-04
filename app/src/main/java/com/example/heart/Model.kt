package com.example.heart

data class HeartDiseaseRequest(
    val chestPainType: Int,
    val heartRate: Int,
    val exang: Int,
    val oldPeak: Float,
    val ca: Int,
    val thalassemia: Int
)
data class PredictionResponse(
    val result: String,
    val features: List<Any>
)
