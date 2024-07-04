package com.example.treecare.service.model

data class RiwayatKerusakanPohonModel (
    var id:                     String? = null,
    var riwayatPohonId:	        String? = null,
    var gambar:                 List<String>? = null,
    var gambarKerusakkan:       List<String>? = null,
    var bagian_pohon:		    String? = null,
    var deskripsi:		        String? = null,
    var potensi_kegagalan:	    Int? = null,
    var ukuran_bagian_pohon:    Int? = null,
    var peringkat_target:	    Int? = null,
    var peringkatBahaya:	    Float? = null,
    var butuh_tindakan:	        Boolean? = null,
    var pemangkasan:		    String? = null,
    var detail_pemangkasan:     String? = null,
    var pohon_dipindahkan:	    Boolean? = null,
    var target_dipindahkan:	    Boolean? = null,
    var saran:			        String? = null,
)