package com.example.treecare.service.api.v1.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RiwayatPohonRequest(
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

    @SerializedName("riwayat_kerusakan_pohon")
    @Expose
    var riwayatKerusakanPohon: List<RiwayatKerusakanPohon>? = null,
)
data class RiwayatKerusakanPohon (

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
