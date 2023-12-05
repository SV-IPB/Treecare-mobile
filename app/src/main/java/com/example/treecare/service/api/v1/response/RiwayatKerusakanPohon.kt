package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RiwayatKerusakanPohon (

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("gambar")
    @Expose
    var gambar: List<String>? = null,

    @SerializedName("bagian_pohon")
    @Expose
    var bagian_pohon: String? = null,

    @SerializedName("deskripsi")
    @Expose
    var deskripsi: String? = null,

    @SerializedName("potensi_kegagalan")
    @Expose
    var potensi_kegagalan: Int? = null,

    @SerializedName("ukuran_bagian_pohon")
    @Expose
    var ukuran_bagian_pohon: Int? = null,

    @SerializedName("peringkat_target")
    @Expose
    var peringkat_target: Int? = null,

    @SerializedName("peringkat_bahaya")
    @Expose
    var peringkat_bahaya: Int? = null,

    @SerializedName("butuh_tindakan")
    @Expose
    var butuh_tindakan: Boolean? = null,

    @SerializedName("pemangkasan")
    @Expose
    var pemangkasan: String? = null,

    @SerializedName("pohon_dipindahkan")
    @Expose
    var pohon_dipindahkan: Boolean? = null,

    @SerializedName("target_dipindahkan")
    @Expose
    var target_dipindahkan: Boolean? = null,

    @SerializedName("saran")
    @Expose
    var saran: String? = null
)