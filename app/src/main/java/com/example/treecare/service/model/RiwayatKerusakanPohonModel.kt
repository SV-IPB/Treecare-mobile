package com.example.treecare.service.model

data class RiwayatKerusakanPohonModel (
    var id:                 String? = null,			
    var riwayatPohonId:	    String? = null,
    var gambarKerusakan:    String? = null,	
    var bagianPohon:		String? = null,
    var deksripsi:		    String? = null,
    var potensiKegagalan:	Int? = null,
    var ukuranBagianPohon:  Int? = null,	
    var peringkatTarget:	Int? = null,
    var peringkatBahaya:	Float? = null,
    var butuhTindakan:	    Boolean? = null,
    var pemangkasan:		String? = null,
    var pohonDipindahkan:	Boolean? = null,
    var targetDipindahkan:	Boolean? = null,
    var saran:			    String? = null,
)