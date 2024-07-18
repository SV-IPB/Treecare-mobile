package com.example.treecare.service.api.v1.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class RiwayatPohonRequest(
    @SerializedName("image")
    @Expose
    val image: List<MultipartBody.Part>,

    @SerializedName("riwayatKerusakanPohon")
    @Expose
    val riwayatKerusakanPohon: MutableList<riwayatKerusakanPohon>,

    @SerializedName("keliling")
    @Expose
    val keliling: Float?,

    @SerializedName("tinggi")
    @Expose
    val tinggi: Float?,

    @SerializedName("lebarTajuk")
    @Expose
    val lebarTajuk: Float?,

    @SerializedName("bentuk")
    @Expose
    val bentuk: String?,

    @SerializedName("liveCrownRatio")
    @Expose
    val liveCrownRatio: Int?,

    @SerializedName("sejarahPemangkasan")
    @Expose
    val sejarahPemangkasan: String?,

    @SerializedName("warnaDaun")
    @Expose
    val warnaDaun: String?,

    @SerializedName("epicormic")
    @Expose
    val epicormic: Boolean?,

    @SerializedName("kerapatanDaun")
    @Expose
    val kerapatanDaun: String?,

    @SerializedName("ukuranDaun")
    @Expose
    val ukuranDaun: String?,

    @SerializedName("woundWood")
    @Expose
    val woundWood: String?,

    @SerializedName("twigDieback")
    @Expose
    val twigDieback: Boolean?,

    @SerializedName("vigor")
    @Expose
    val vigor: String?,

    @SerializedName("hama")
    @Expose
    val hama: String?,

    @SerializedName("karakteristikTapak")
    @Expose
    val karakteristikTapak: String?,

    @SerializedName("gangguan")
    @Expose
    val gangguan: Boolean?,

    @SerializedName("masalahTanah")
    @Expose
    val masalahTanah: String?,

    @SerializedName("gangguanLain")
    @Expose
    val gangguanLain: String?,

    @SerializedName("pemanfaatanSekitar")
    @Expose
    val pemanfaatanSekitar: String?,

    @SerializedName("dapatDipindahkan")
    @Expose
    val dapatDipindahkan: Boolean?,

    @SerializedName("dapatDibatasi")
    @Expose
    val dapatDibatasi: Boolean?,

    @SerializedName("hunian")
    @Expose
    val hunian: String?
)
data class riwayatKerusakanPohon (
    @SerializedName("gambar")
    @Expose
    val gambar: List<String>,

    @SerializedName("deskripsi")
    @Expose
    val deskripsi: String,

    @SerializedName("bagian_pohon")
    @Expose
    val bagian_pohon: String,

    @SerializedName("potensi_kegagalan")
    @Expose
    val potensi_kegagalan: Int,

    @SerializedName("ukuran_bagian_pohon")
    @Expose
    val ukuran_bagian_pohon: Int,

    @SerializedName("peringkat_target")
    @Expose
    val peringkat_target: Int,

    @SerializedName("butuh_tindakan")
    @Expose
    val butuh_tindakan: Boolean,

    @SerializedName("pemangkasan")
    @Expose
    val pemangkasan: String,

    @SerializedName("detail_pemangkasan")
    @Expose
    val detail_pemangkasan: String,

    @SerializedName("pohon_dipindahkan")
    @Expose
    val pohon_dipindahkan: Boolean,

    @SerializedName("target_dipindahkan")
    @Expose
    val target_dipindahkan: Boolean,

    @SerializedName("saran")
    @Expose
    val saran: String
)
