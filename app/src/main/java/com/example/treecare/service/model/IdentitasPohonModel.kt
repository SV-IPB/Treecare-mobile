package com.example.treecare.service.model

data class IdentitasPohonModel (
    var id:             String? = null,
    var nomorPohon:     String? = null,
    var gambar:         String? = null,
    var alamat:         String? = null,
    var latitude:       Float? = null,
    var longitude:      Float? = null,
    var namaProjek:     String? = null,
    var pemilik:        String? = null,
    var jenis:          String? = null,
    var nilaiSpesial:   String?  = null
)
    // constructor()
    // constructor(
    //     id:             String?,
    //     nomor_pohon:    Int?,
    //     gambar:         String?,
    //     alamat:         String?,
    //     latitude:       String?,
    //     longitude:      String?,
    //     nama_projek:    String?,
    //     pemilik:        String?,
    //     jenis:          String?,
    //     nilai_spesial:  String?
    // ) {
    //     this.id             = id
    //     this.nomor_pohon    = nomor_pohon
    //     this.gambar         = gambar
    //     this.alamat         = alamat
    //     this.latitude       = latitude
    //     this.longitude      = longitude
    //     this.nama_projek    = nama_projek
    //     this.pemilik        = pemilik
    //     this.jenis          = jenis
    //     this.nilai_spesial  = nilai_spesial
    // }
// }