package com.efrinaldi.zwallet.ui.layout.main.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.efrinaldi.zwallet.data.ZWalletDataSource
import com.efrinaldi.zwallet.model.*
import com.efrinaldi.zwallet.model.request.TransferRequest
import com.efrinaldi.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TransferViewModel @Inject constructor(private var  dataSource : ZWalletDataSource): ViewModel(){

    private var selectedContact = MutableLiveData<Contact>()
    private var transfer = MutableLiveData<TransferRequest>()

    fun getContactUser(): LiveData<Resource<APIResponse<List<Contact>>?>> {
        return dataSource.getContactUser()
    }

    fun setSelectedContact(contact: Contact) {
        selectedContact.value = contact
    }

    fun getSelectedContact(): MutableLiveData<Contact> {
        return selectedContact
    }

    fun setTransferParameter(data: TransferRequest) {
        transfer.value = data
    }

    fun getTransferParameter(): MutableLiveData<TransferRequest> {
        return transfer
    }

    fun getBalance() : LiveData<Resource<APIResponse<List<UserDetail>>?>> {
        return dataSource.getBalance()
    }

    fun transfer(data: TransferRequest, pin: String): LiveData<Resource<APITransfer<Transfer>?>> {
        return dataSource.transfer(data, pin)
    }

}