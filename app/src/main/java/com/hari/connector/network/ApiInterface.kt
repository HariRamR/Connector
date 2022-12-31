package com.hari.connector.network

import com.hari.connector.models.ContactModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("users")
    fun getContactData(@Query("page") page:String): Observable<Response<ContactModel>>
}