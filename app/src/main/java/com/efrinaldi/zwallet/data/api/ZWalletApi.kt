package com.efrinaldi.zwallet.data.api

import com.efrinaldi.zwallet.model.*
import com.efrinaldi.zwallet.model.request.*
import retrofit2.Call
import retrofit2.http.*

interface ZWalletApi {
    @POST("auth/login")
    suspend fun login(@Body request:LoginRequest): APIResponse<User>

    @POST("auth/signup")
    fun register(@Body request:RegisterRequest): Call<APIResponse<User>>

    @GET("user/myProfile")
    suspend fun getProfile(): APIResponse<UserDetail>

    @GET("home/getBalance")
    suspend fun getBalance(): APIResponse<List<UserDetail>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<APIResponse<User>>

    @GET("home/getInvoice")
    suspend fun getInvoice(): APIResponse<List<Invoice>>

    @GET("tranfer/contactUser")
    suspend fun getContactUser(): APIResponse<List<Contact>>

    @POST("tranfer/newTranfer")
    suspend fun transfer(@Body request: TransferRequest, @Header("x-access-PIN") pin: String): APITransfer<Transfer>

    @PATCH("auth/PIN")
    suspend fun setPin(@Body request: SetPinRequest): APIResponse<String>

    @PATCH("/user/changePassword")
    fun changePassword(@Body request: ChangePasswordRequest): Call <APIResponse<User>>

}