package com.example.heart.BACKENED

data class HeartDiseaseRequest(
    val chestPainType: Int,
    val heartRate: Int,
    val exang: Int,
    val oldPeak: Float,
    val ca: Int,
    val thalassemia: Int
)
data class PredictionResponse(
    val prediction: String,
    val features: List<Any>
)
