package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AllIdentitasPohonResponse(
    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("code")
    @Expose
    var code: Int? = null,

    @SerializedName("data")
    @Expose
    var data: datas? = null,
)

data class datas(
    @SerializedName("data")
    @Expose
    var datas: List<IdentitasPohon>? = null,
)
