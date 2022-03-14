package com.efrinaldi.zwallet.ui.auth.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.efrinaldi.zwallet.data.ZWalletDataSource
import com.efrinaldi.zwallet.data.api.ZWalletApi
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.User
import com.efrinaldi.zwallet.network.NetworkConfig
import com.efrinaldi.zwallet.utils.Resource

class LoginViewModel (app: Application): ViewModel() {
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    // ZwalletDataSource -> login()
    // return State.Loading
    // try -> catch
    // return State.Success
    // else return State.Error



    fun login(email: String, password: String) : LiveData<Resource<APIResponse<User>?>> {
        return dataSource.login(email, password)
    }

}