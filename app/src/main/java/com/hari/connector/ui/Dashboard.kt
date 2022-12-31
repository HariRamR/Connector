package com.hari.connector.ui

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hari.connector.adapters.ContactAdapter
import com.hari.connector.databinding.ActivityDashboardBinding
import com.hari.connector.factories.ContactFactory
import com.hari.connector.interfaces.ContactInterface
import com.hari.connector.models.Data
import com.hari.connector.ui.utils.CheckInternet
import com.hari.connector.viewmodels.ContactViewModel
import io.reactivex.disposables.CompositeDisposable

class Dashboard : AppCompatActivity(), ContactInterface {

    private val disposable = CompositeDisposable()
    private var isLinearManager = true
    private var contactList = listOf<Data>()
    private var view: ActivityDashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(view!!.root)

        val linearLayoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        view!!.recyclerDashboard.layoutManager = linearLayoutManager
        var contactAdapter = ContactAdapter(this)
        view!!.recyclerDashboard.adapter = contactAdapter

        view!!.radioGrpDashboard.setOnCheckedChangeListener { _, _ ->
            val idRadioButtonChosen: Int = view!!.radioGrpDashboard.checkedRadioButtonId
            if (idRadioButtonChosen > 0) {
                val radioButtonChosen = findViewById<View>(idRadioButtonChosen) as RadioButton
                if (radioButtonChosen.text.toString() == "Grid") {

                    if (isLinearManager) {

                        isLinearManager = !isLinearManager
                        view!!.recyclerDashboard.layoutManager = gridLayoutManager
                        contactAdapter = ContactAdapter(this)
                        view!!.recyclerDashboard.adapter = contactAdapter
                        contactAdapter.updateGridItems(contactList, isLinearManager)
                    }
                } else if (!isLinearManager) {

                    isLinearManager = !isLinearManager
                    view!!.recyclerDashboard.layoutManager = linearLayoutManager
                    contactAdapter = ContactAdapter(this)
                    view!!.recyclerDashboard.adapter = contactAdapter
                    contactAdapter.updateGridItems(contactList, isLinearManager)
                }
            }
        }

        val viewModel =
            ViewModelProvider(this, ContactFactory(application))[ContactViewModel::class.java]

        viewModel.getContactsData().observe(this) {

                contacts ->
            contactList = contacts
            contactAdapter.updateGridItems(contacts, isLinearManager)
        }

        viewModel.isNoData().observe(this) { isNoData ->
            if (isNoData) {
                if (CheckInternet.isInternetAvailable(this)) {
                    view!!.recyclerDashboard.visibility = View.GONE
                    view!!.noDataDashboard.visibility = View.VISIBLE
                } else {
                    view!!.recyclerDashboard.visibility = View.GONE
                    view!!.errorDashboard.visibility = View.VISIBLE
                }
            } else {
                view!!.noDataDashboard.visibility = View.GONE
                view!!.errorDashboard.visibility = View.GONE
                view!!.recyclerDashboard.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading().observe(this) { isProgressVisible ->
            if (isProgressVisible) {
                view!!.progressBarDashboard.visibility = View.VISIBLE
            } else view!!.progressBarDashboard.visibility = View.GONE
        }
        viewModel.isLoading.value = true
        viewModel.initContactRepo(disposable, this)
    }

    override fun makeFABVisible() {

        if (view != null) {

            view!!.editFabDashboard.visibility = View.VISIBLE
        }
    }

    override fun makeFABInVisible() {

        if (view != null) {

            view!!.editFabDashboard.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}