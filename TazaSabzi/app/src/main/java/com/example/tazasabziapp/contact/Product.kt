package com.example.tazasabziapp.contact

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("productId") var productId:String="",
    @SerializedName("name") var name:String="",
    @SerializedName("price") var price:String="",
)
