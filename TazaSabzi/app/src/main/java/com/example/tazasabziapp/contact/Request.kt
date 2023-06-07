package com.example.tazasabziapp.contact

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("action") var action:String="",
    @SerializedName("userId") var userId:String="",
    @SerializedName("userName") var userName:String="",
    @SerializedName("userEmail") var userEmail:String="",
    @SerializedName("userPassword") var userPassword:String="",
    @SerializedName("productId") var productId:String="",
    @SerializedName("name") var name:String="",
    @SerializedName("price") var price:String=""
)
