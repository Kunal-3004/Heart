package com.example.heart.BACKENED.skin

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SkinDiseaseApi {
    @Multipart
    @POST("skin-disease")
    suspend fun detectSkinDisease(
        @Part image: MultipartBody.Part
    ): Response<SkinDiseaseResponse>
}
