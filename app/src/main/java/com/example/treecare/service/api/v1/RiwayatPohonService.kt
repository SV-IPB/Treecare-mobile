package com.example.treecare.service.api.v1

import com.example.treecare.service.api.v1.request.RiwayatPohonRequest
import com.example.treecare.service.api.v1.response.RiwayatPohonsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RiwayatPohonService {

    @GET("pohon/{id}/riwayat")
    fun getAllRiwayatPohonById (
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsResponse>

    @POST("pohon/{id}/riwayat")
    fun createHistory(
        @Body request: RiwayatPohonRequest,
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsResponse>
}