package com.example.treecare.service.api.v1

import com.example.treecare.service.api.v1.request.IdentitasPohonRequest
import com.example.treecare.service.api.v1.response.IdentitasPohonResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PohonService {
    @GET("pohon/nomor-pohon/{nomor_pohon}")
    fun getByNomorPohon(
        @Path("nomor_pohon") nomor_pohon: String?,
        @Header("Authorization") accessToken: String?
    ):Call<IdentitasPohonResponse>

    @POST("pohon")
    fun createPohon(
        @Body request: IdentitasPohonRequest,
        @Header("Authorization") accessToken: String?
    ):Call<IdentitasPohonResponse>

    @PUT("pohon/{id}")
    fun updatePohon(
        @Path("id") id: String?,
        @Body request: IdentitasPohonRequest,
        @Header("Authorization") accessToken: String?
    ):Call<IdentitasPohonResponse>

    @GET("pohon/{id}/audit")
    fun getAudit(
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<String>
}