package com.hari.connector.repos

import android.content.Context
import android.util.Log
import com.hari.connector.models.Data
import com.hari.connector.network.ApiClient
import com.hari.connector.ui.utils.CheckInternet
import com.hari.connector.viewmodels.ContactViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class ContactRepo(
    private val disposable: CompositeDisposable,
    private val viewModel: ContactViewModel
) {

    private var networkContactList: ArrayList<Data> = arrayListOf()

    fun getSourceFromAPI(context: Context, pagePos: Int) {

        if (!CheckInternet.isInternetAvailable(context)) {
            doResultAction()
        } else
            disposable.add(
                ApiClient.getClient()!!.getContactData(pagePos.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { contact ->

                            val contactModel = contact.body()
                            networkContactList.addAll(contactModel!!.data)
                            val pageMaxSize = contactModel.totalPages
                            if (pageMaxSize != null && pageMaxSize > 1 && pagePos < pageMaxSize) {
                                getSourceFromAPI(context, pagePos + 1)
                            } else {
                                doResultAction()
                            }
                        },
                        { throwable ->
                            Log.e("getSourceFromAPI ", throwable.message ?: "onError")
                            viewModel.isLoading.value = false
                        }
                    )
            )
    }

    private fun doResultAction() {
        runBlocking {

            val contactList: List<Data>
            if (networkContactList.isEmpty()) {
                contactList = getAllContacts()
            } else {
                contactList = networkContactList
                deleteContacts()
                insertContacts(networkContactList)
            }
            if (contactList.isEmpty()) {

                viewModel.isNoData.value = true
                viewModel.contactList.value = listOf()
            } else {

                viewModel.isNoData.value = false
                viewModel.contactList.value = contactList
            }
        }
        viewModel.isLoading.value = false
    }

    private fun insertContacts(contacts: ArrayList<Data>) {
        GlobalScope.async {
            viewModel.dao!!.insertContacts(contacts)
        }
    }

    private suspend fun deleteContacts() {

        val deleteDeferred = ArrayList<Deferred<Int>>()
        deleteDeferred.add(GlobalScope.async {
            viewModel.dao!!.deleteContacts()
        })
        deleteDeferred.awaitAll()
    }

    private suspend fun getAllContacts(): List<Data> {

        val selectDeferred = ArrayList<Deferred<List<Data>>>()
        selectDeferred.add(GlobalScope.async {
            viewModel.dao!!.getAllContacts()
        })
        return selectDeferred.awaitAll()[0]
    }
}