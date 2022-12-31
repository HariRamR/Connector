package com.hari.connector.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hari.connector.viewmodels.ContactViewModel

class ContactFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(application) as T
        }
        return super.create(modelClass)
    }
}