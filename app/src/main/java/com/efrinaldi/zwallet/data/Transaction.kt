package com.efrinaldi.zwallet.data

import android.graphics.drawable.Drawable

data class Transaction (
    val transactionImage: Drawable,
    val transactionName: String,
    val transactiontype: String,
    val transactionNominal: Double,
)