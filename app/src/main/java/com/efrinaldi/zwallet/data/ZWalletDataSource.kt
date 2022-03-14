package com.efrinaldi.zwallet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.efrinaldi.zwallet.data.api.ZWalletApi
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.Balance
import com.efrinaldi.zwallet.model.Invoice
import com.efrinaldi.zwallet.model.User
import com.efrinaldi.zwallet.model.request.LoginRequest
import com.efrinaldi.zwallet.utils.Resource
import kotlinx.coroutines.Dispatchers

class ZWalletDataSource(private val apiClient: ZWalletApi) {
    fun login(email: String, password: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            val loginRequest = LoginRequest(email = email, password = password)
            val response = apiClient.login(loginRequest)
            emit(Resource.success(response))
        } catch (e:Exception){
         emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getInvoice() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getInvoice()
            emit(Resource.success(response))
        } catch (e: Exception){
            emit(Resource.error(null,e.localizedMessage))
        }
    }

    fun getBalance() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getBalance()
            emit(Resource.success(response))
        } catch (e: Exception){
            emit(Resource.error(null,e.localizedMessage))
        }
    }
}