package com.example.treecare.service.api.v1

import com.example.treecare.service.api.v1.request.LoginRequest
import com.example.treecare.service.api.v1.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<UserResponse>

    @POST("auth/refresh-token")
    fun refreshToken(
        @Header("Authorization") refreshToken: String?
    ): Call<UserResponse>

    @GET("auth/current-user")
    fun  currentUser(
        @Header("Authorization") authToken: String?
    ): Call<UserResponse>

    @POST("auth/logout")
    fun  logOut(
        @Header("Authorization") authToken: String?
    ): Call<UserResponse>
}