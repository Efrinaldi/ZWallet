package com.efrinaldi.zwallet.model.request

import com.google.gson.annotations.SerializedName

data class SetPinRequest(
    @SerializedName("PIN")
    val PIN: String,
)