package com.example.treecare.service.api.v1

import com.example.treecare.service.api.v1.response.IdentitasPohonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PohonService {
    @GET("pohon/nomor-pohon/{nomor_pohon}")
    fun getByNomorPohon(
        @Path("nomor_pohon") nomor_pohon: String?,
        @Header("Authorization") accessToken: String?
    ):Call<IdentitasPohonResponse>
}