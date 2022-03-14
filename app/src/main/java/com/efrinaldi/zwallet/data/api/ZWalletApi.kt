package com.efrinaldi.zwallet.data.api

import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.Balance
import com.efrinaldi.zwallet.model.Invoice
import com.efrinaldi.zwallet.model.User
import com.efrinaldi.zwallet.model.request.LoginRequest
import com.efrinaldi.zwallet.model.request.RefreshTokenRequest
import com.efrinaldi.zwallet.model.request.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ZWalletApi {
    @POST("auth/login")
    suspend fun login(@Body request:LoginRequest): APIResponse<User>

    @POST("auth/signup")
    fun register(@Body request:RegisterRequest): Call<APIResponse<User>>

    @GET("home/getBalance")
    suspend fun getBalance(): APIResponse<List<Balance>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<APIResponse<User>>

    @GET("home/getInvoice")
    suspend fun getInvoice(): APIResponse<List<Invoice>>
}