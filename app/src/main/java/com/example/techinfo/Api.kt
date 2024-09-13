package com.example.techinfo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.example.techinfo.Fragments.Troubleshoot_modules.TroubleShoot_data
import retrofit2.Call

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.12:3000/" // Replace with your actual server address

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(Api::class.java)
    }
}

interface Api {
    @GET("get_articles.php") // Replace with the actual endpoint path
    fun getArticles(): Call<List<TroubleShoot_data>>
}
