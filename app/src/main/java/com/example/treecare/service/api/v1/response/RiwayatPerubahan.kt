package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RiwayatPerubahan (

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("pohon_id")
    @Expose
    var pohonId: String? = null,

    @SerializedName("user")
    @Expose
    var user: User? = null,

    @SerializedName("field_name")
    @Expose
    var fieldName: String? = null,

    @SerializedName("tanggal")
    @Expose
    var tanggal: String? = null,

    @SerializedName("jam")
    @Expose
    var jam: String? = null

)