package com.efrinaldi.zwallet.ui.layout.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.efrinaldi.zwallet.data.ZWalletDataSource
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.User
import com.efrinaldi.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(private val dataSource: ZWalletDataSource) : ViewModel() {

    fun otpActivation(email: String, otp: String): LiveData<Resource<APIResponse<User>?>> {
        return dataSource.otpActivation(email, otp)
    }

}