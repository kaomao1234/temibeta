package com.example.temi_beta.api

import fuel.HttpResponse
import fuel.httpPut

suspend fun confirm_order(status: String): Boolean? {
    val body = mapOf(
        "order_status" to status
    )
    val response: HttpResponse =
        "https://fastapideta-1-v4049125.deta.app/confirm_order/{orders}".httpPut(body = body.toString())
    return if(response.statusCode == 200){
        true
    }else{
        null
    }

}