package com.efrinaldi.zwallet.ui.layout.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.efrinaldi.zwallet.data.ZWalletDataSource
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.UserDetail
import com.efrinaldi.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {

    fun getBalance(): LiveData<Resource<APIResponse<List<UserDetail>>?>> {
        return dataSource.getBalance()
    }
}