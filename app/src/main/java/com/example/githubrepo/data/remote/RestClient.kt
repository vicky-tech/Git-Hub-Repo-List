package com.example.githubrepo.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RestClient {
    private var apiRestService: ApiService
    val moshi : Moshi = Moshi.Builder()
       .add(KotlinJsonAdapterFactory())
       .build()

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/orgs/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        apiRestService = retrofit.create(ApiService::class.java)


    }

    fun getApiService(): ApiService {
        return apiRestService
    }
}