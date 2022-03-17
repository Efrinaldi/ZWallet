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
class PersonalInformationViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {

    fun getProfileInfo(): LiveData<Resource<APIResponse<UserDetail>?>> {
        return dataSource.getProfileInfo()
    }


}