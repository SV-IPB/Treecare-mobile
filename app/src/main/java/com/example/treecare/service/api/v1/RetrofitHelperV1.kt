package com.example.treecare.service.api.v1

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

const val BASEURL = "hppt://localhost/api/v1/"
class RetrofitHelperV1 {

    private var retrofit: Retrofit? = null

    fun getApiClient(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return retrofit!!
    }

    fun getApiClientAuth(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }

        return retrofit!!
    }

}