package com.hari.connector.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hari.connector.database.ConnectorDatabase
import com.hari.connector.database.ContactDAO
import com.hari.connector.models.Data
import com.hari.connector.repos.ContactRepo
import io.reactivex.disposables.CompositeDisposable

class ContactViewModel(application: Application) : ViewModel() {

    var contactList: MutableLiveData<List<Data>> = MutableLiveData<List<Data>>()
    private var contactRepo: ContactRepo? = null
    var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    var isNoData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var database: ConnectorDatabase? = null
    var dao: ContactDAO? = null

    init {
        database = ConnectorDatabase.getInstance(application)
        dao = database!!.getContactDAO()
    }

    fun initContactRepo(disposable: CompositeDisposable, context: Context) {
        contactRepo = ContactRepo(disposable, this)
        contactRepo!!.getSourceFromAPI(context, 1)
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun isNoData(): LiveData<Boolean> {
        return isNoData
    }

    fun getContactsData(): LiveData<List<Data>> {
        return contactList
    }
}