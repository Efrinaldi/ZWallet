package com.efrinaldi.zwallet.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstname")
    val firstname: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("lastname")
    val lastname: String?,
    @SerializedName("balance")
    val balance: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?
)