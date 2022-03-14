package com.efrinaldi.zwallet.model.request

data class RefreshTokenRequest (
    val email: String,
    val refreshtoken: String
)
