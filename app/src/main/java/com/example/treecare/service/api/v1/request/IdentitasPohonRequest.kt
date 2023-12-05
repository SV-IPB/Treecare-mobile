package com.example.treecare.service.api.v1.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IdentitasPohonRequest (
    @SerializedName("nomor_pohon")
    @Expose
    var nomorPohon: String? = null,

    @SerializedName("alamat")
    @Expose
    var alamat: String? = null,

    @SerializedName("latitude")
    @Expose
    var latitude: Float? = null,

    @SerializedName("longitude")
    @Expose
    var longitude: Float? = null,

    @SerializedName("nama_proyek")
    @Expose
    var namaProyek: String? = null,

    @SerializedName("nama_pemilik")
    @Expose
    var namaPemilik: String? = null,

    @SerializedName("jenis_pohon")
    @Expose
    var jenisPohon: String? = null,

    @SerializedName("nilai_spesial")
    @Expose
    var nilaiSpesial: String? = null,

    @SerializedName("gambar")
    @Expose
    var gambar: String? = null,
)