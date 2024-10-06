package com.example.heart.BACKENED

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/quick")
    fun getPrediction(@Body request: HeartDiseaseRequest): Call<PredictionResponse>
}
