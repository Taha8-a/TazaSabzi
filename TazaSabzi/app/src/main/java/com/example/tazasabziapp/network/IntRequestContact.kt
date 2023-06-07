package com.example.tazasabziapp.network

import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IntRequestContact {

    @POST("service.php")
    fun makeApiCall(@Body request: Request): Call<Response>
}