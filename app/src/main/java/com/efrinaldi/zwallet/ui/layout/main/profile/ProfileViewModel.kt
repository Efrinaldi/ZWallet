package com.efrinaldi.zwallet.ui.layout.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.efrinaldi.zwallet.data.ZWalletDataSource
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.User
import com.efrinaldi.zwallet.model.UserDetail
import com.efrinaldi.zwallet.model.request.ManagePhoneRequest
import com.efrinaldi.zwallet.model.request.SetPinRequest
import com.efrinaldi.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {

    fun getBalance(): LiveData<Resource<APIResponse<List<UserDetail>>?>> {
        return dataSource.getBalance()
    }

    fun checkPin(pin: Int): LiveData<Resource<APIResponse<String>?>> {
        return dataSource.checkPin(pin)
    }

    fun setPin(request: SetPinRequest): LiveData<Resource<APIResponse<String>?>> {
        return dataSource.setPin(request)
    }

    fun managePhone(request: ManagePhoneRequest): LiveData<Resource<APIResponse<User>?>> {
        return dataSource.managePhone(request)
    }
}