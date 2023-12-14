package com.example.treecare.service.model

data class RiwayatPerubahanModel (

    var id: String? = null,

    var pohonId: String? = null,

    var user: UserModel? = null,

    var fieldName: String? = null,

    var tanggal: String? = null,

    var jam: String? = null

)