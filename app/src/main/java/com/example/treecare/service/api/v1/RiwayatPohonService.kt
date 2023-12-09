package com.example.treecare.service.api.v1

import com.example.treecare.service.api.v1.request.RiwayatPohonRequest
import com.example.treecare.service.api.v1.response.RiwayatKerusakanPohonResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonsPagingResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonsResponse
import retrofit2.Call
import retrofit2.http.*

interface RiwayatPohonService {

    /**
     * CreateHistory
     */
    @GET("pohon/{id}/riwayat")
    fun getAllRiwayatPohonById (
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsResponse>

    /**
     * CreateHistory
     */
    @GET("pohon/riwayat/{id}")
    fun getRiwayatDetailPohonById (
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonResponse>

    @POST("pohon/{id}/riwayat")
    fun createHistory(
        @Body request: RiwayatPohonRequest,
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsResponse>

    /**
     *
     */
    @GET("pohon/allriwayat")
    fun getAllRiwayat(
        @Query("sort") sort: String,
        @Query("sortType") sortType: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsPagingResponse>

    @GET("pohon/allriwayat")
    fun getAllRiwayatByKeyword(
        @Query("keyword") keyword: String,
        @Query("sort") sort: String,
        @Query("sortType") sortType: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsPagingResponse>

    @GET("pohon/kerusakan/{id}")
    fun getKerusakanDetail(
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatKerusakanPohonResponse>
}