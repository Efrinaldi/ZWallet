package com.efrinaldi.zwallet.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.efrinaldi.zwallet.data.ZWalletDataSource
import com.efrinaldi.zwallet.data.api.ZWalletApi
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.Balance
import com.efrinaldi.zwallet.model.Invoice
import com.efrinaldi.zwallet.network.NetworkConfig
import com.efrinaldi.zwallet.utils.Resource

class HomeViewModel (app: Application): ViewModel(){
    private val invoices = MutableLiveData<List<Invoice>>()
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    fun getInvoice(): LiveData<Resource<APIResponse<List<Invoice>>?>> {
        return dataSource.getInvoice()
    }
    fun getBalance(): LiveData<Resource<APIResponse<List<Balance>>?>> {
        return dataSource.getBalance()
    }

}