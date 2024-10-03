package com.example.heart

data class HeartDiseaseRequest(
    val SEX: Int,
    val age: Int,
    val Current_Smoker: Int,
    val Chest_Pain_Type: Int,
    val Stroke: Int,
    val BP_medication: Int,
    val Diabetes: Int,
    val Height: Float,
    val Weight: Float,
    val Heart_Rate: Int,
    val Exang: Int,
    val Oldpeak: Float,
    val ca: Int,
    val thalassemia: Int
)
data class PredictionResponse(
    val result: String,
    val sex: String,
    val age: Int,
    val currentSmoker: Boolean,
    val stroke: Boolean,
    val exang: Boolean,
    val diabetes: Boolean,
    val bpMedication: Boolean,
    val bmi: Float,
    val maxHeartRate: Int
)
