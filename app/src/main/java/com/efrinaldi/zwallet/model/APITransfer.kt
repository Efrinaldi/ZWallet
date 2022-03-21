package com.efrinaldi.zwallet.model

data class APITransfer<T>(
    var status: Int,
    var message: String,
    var details: T?
)