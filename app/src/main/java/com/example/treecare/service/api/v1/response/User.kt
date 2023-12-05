package com.example.treecare.service.api.v1.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null
)