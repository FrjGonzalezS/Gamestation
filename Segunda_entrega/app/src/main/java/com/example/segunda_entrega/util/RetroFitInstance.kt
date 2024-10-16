package com.example.segunda_entrega.util

import com.example.segunda_entrega.servicio.ApiServicio
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiServicio by lazy {
        Retrofit.Builder()
            .baseUrl("https://programadormaldito.cl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicio::class.java)
    }
}

