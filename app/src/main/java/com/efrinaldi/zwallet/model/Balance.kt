package com.efrinaldi.zwallet.model


import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("balance")
    val balance: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName( "firstname")
    val firstname: String?,
    @SerializedName( "lastname")
    val lastname: String?

)