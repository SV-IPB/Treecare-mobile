package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RiwayatKerusakanPohonResponse (

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("code")
    @Expose
    var code: Int? = null,

    @SerializedName("data")
    @Expose
    var data: List<RiwayatKerusakanPohon>? = null

)