package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse  {

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    data class Data (

        @SerializedName("access_token")
        @Expose
        var access_token: String? = null,

        @SerializedName("refresh_token")
        @Expose
        var refresh_token: String? = null,

        @SerializedName("user")
        @Expose
        var user: User? = null
    )
}