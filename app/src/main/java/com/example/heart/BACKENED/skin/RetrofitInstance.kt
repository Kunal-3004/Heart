package com.example.heart.BACKENED.skin

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://detect-skin-disease1.p.rapidapi.com/"

    val api: SkinDiseaseApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()

        retrofit.create(SkinDiseaseApi::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-host", "detect-skin-disease1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "790108fef3msh9a03acc9bd524a4p17ce95jsn5c6972e306c")
                    .build()
                chain.proceed(request)
            }
            .build()
    }
}
