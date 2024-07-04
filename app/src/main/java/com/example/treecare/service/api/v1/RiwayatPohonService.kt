package com.example.treecare.service.api.v1

import com.example.treecare.service.api.v1.request.RiwayatPohonRequest
import com.example.treecare.service.api.v1.request.riwayatKerusakanPohon
import com.example.treecare.service.api.v1.response.RiwayatKerusakanPohonResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonsPagingResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RiwayatPohonService {

    @GET("pohon/{id}/riwayat")
    fun getAllRiwayatPohonById (
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?
    ): Call<RiwayatPohonsResponse>

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

    @Multipart
    @POST("pohon/{id}/riwayat")
    fun createHistoryV2(
        @Path("id") id: String?,
        @Header("Authorization") accessToken: String?,
        @Part image: List<MultipartBody.Part>,
        @Part("riwayatKerusakanPohon") riwayatKerusakanPohon: MutableList<riwayatKerusakanPohon>,
        @Part("keliling") keliling: Float,
        @Part("tinggi") tinggi: Float,
        @Part("lebarTajuk") lebarTajuk: Float,
        @Part("bentuk") bentuk: String,
        @Part("liveCrownRatio") liveCrownRatio: Int,
        @Part("sejarahPemangkasan") sejarahPemangkasan: String,
        @Part("warnaDaun") warnaDaun: String,
        @Part("epicormic") epicormic: Boolean,
        @Part("kerapatanDaun") kerapatanDaun: String,
        @Part("ukuranDaun") ukuranDaun: String,
        @Part("woundWood") woundWood: String,
        @Part("twigDieback") twigDieback: Boolean,
        @Part("vigor") vigor: String,
        @Part("hama") hama: String,
        @Part("karakteristikTapak") karakteristikTapak: String,
        @Part("gangguan") gangguan: Boolean,
        @Part("masalahTanah") masalahTanah: String,
        @Part("gangguanLain") gangguanLain: String,
        @Part("pemanfaatanSekitar") pemanfaatanSekitar: String,
        @Part("dapatDipindahkan") dapatDipindahkan: Boolean,
        @Part("dapatDibatasi") dapatDibatasi: Boolean,
        @Part("hunian") hunian: String
    ): Call<RiwayatPohonResponse>

}