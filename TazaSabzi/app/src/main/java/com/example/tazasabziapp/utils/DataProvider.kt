package com.example.tazasabziapp.utils

import com.example.tazasabziapp.contact.Product
import com.example.tazasabziapp.contact.Response

object DataProvider {
    var response: Response = Response()
    var product: Product = Product()
    lateinit var userId:String
    lateinit var userName:String
    lateinit var userEmail:String
    lateinit var userPassword:String
}