package com.efrinaldi.zwallet.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("email")
    val email: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("balance")
    val balance: Int?,
    @SerializedName("phone")
    val phone: String?
)