package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RiwayatPohonsPagingResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
}

data class Data (
    @SerializedName("data")
    @Expose
    var data: List<RiwayatPohon>? = null,

    @SerializedName("page")
    @Expose
    var page: Int? = null,

    @SerializedName("page_size")
    @Expose
    var pageSize: Int? = null,

    @SerializedName("total_data")
    @Expose
    var totalData: Int? = null,

    @SerializedName("total_page")
    @Expose
    var totalPage: Int? = null
)