package com.example.tazasabziapp.contact

data class Response(
    var status:Boolean = false,
    var responseCode:Int = -1,
    var message:String = "",
    var userId:String = "",
    var productId:String = "",
    var allProducts:MutableList<Product> = mutableListOf(),
    var myProducts:MutableList<Product> = mutableListOf()
)
