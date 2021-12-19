package uz.pdp.youtube.retro

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    const val BASE_URL = "https://www.googleapis.com/youtube/"

    fun getData(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val appService = getData().create(ApiService::class.java)
}