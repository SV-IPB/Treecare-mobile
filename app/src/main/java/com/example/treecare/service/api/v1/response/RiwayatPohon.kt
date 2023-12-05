package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RiwayatPohon (

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("keliling")
    @Expose
    var keliling: Float? = null,

    @SerializedName("tinggi")
    @Expose
    var tinggi: Float? = null,

    @SerializedName("lebar_tajuk")
    @Expose
    var lebarTajuk: Float? = null,

    @SerializedName("bentuk")
    @Expose
    var bentuk: String? = null,

    @SerializedName("live_crown_ratio")
    @Expose
    var liveCrownRatio: Int? = null,

    @SerializedName("sejarah_pemangkasan")
    @Expose
    var sejarahPemangkasan: String? = null,

    @SerializedName("warna_daun")
    @Expose
    var warnaDaun: String? = null,

    @SerializedName("epicormic")
    @Expose
    var epicormic: Boolean? = null,

    @SerializedName("kerapatan_daun")
    @Expose
    var kerapatanDaun: String? = null,

    @SerializedName("ukuran_daun")
    @Expose
    var ukuranDaun: String? = null,

    @SerializedName("wound_wood")
    @Expose
    var woundWood: String? = null,

    @SerializedName("twig_dieback")
    @Expose
    var twigDieback: Boolean? = null,

    @SerializedName("vigor")
    @Expose
    var vigor: String? = null,

    @SerializedName("hama")
    @Expose
    var hama: String? = null,

    @SerializedName("karakteristik_tapak")
    @Expose
    var karakteristikTapak: String? = null,

    @SerializedName("gangguan")
    @Expose
    var gangguan: Boolean? = null,

    @SerializedName("masalah_tanah")
    @Expose
    var masalahTanah: String? = null,

    @SerializedName("gangguan_lain")
    @Expose
    var gangguanLain: String? = null,

    @SerializedName("pemanfaatan_sekitar")
    @Expose
    var pemanfaatanSekitar: String? = null,

    @SerializedName("dapat_dipindahkan")
    @Expose
    var dapatDipindahkan: Boolean? = null,

    @SerializedName("dapat_dibatasi")
    @Expose
    var dapatDibatasi: Boolean? = null,

    @SerializedName("hunian")
    @Expose
    var hunian: String? = null,


    @SerializedName("tanggal")
    @Expose
    var tanggal: String? = null,

    @SerializedName("jam")
    @Expose
    var jam: String? = null,

    @SerializedName("riwayat_kerusakan_pohon")
    @Expose
    var riwayatKerusakanPohon: List<RiwayatKerusakanPohon>? = null,

    @SerializedName("identitas_pohon")
    @Expose
    var identitasPohon: IdentitasPohon? = null,

    @SerializedName("user")
    @Expose
    var user: User? = null

)
