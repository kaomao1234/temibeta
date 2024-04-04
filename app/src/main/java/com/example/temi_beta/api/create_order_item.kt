package com.example.temi_beta.api

import android.util.Log
import com.beust.klaxon.JsonObject
import fuel.httpPost

suspend fun createOrderItem(itemId: String, quantity: String): Boolean {
    val body = mapOf<String, String>(
        "item_id" to itemId,
        "quantity" to quantity
    )
    val response = "https://fastapideta-1-v4049125.deta.app/update_quantity/".httpPost(
        body = JsonObject(body).toJsonString(),
        headers = mapOf("Content-Type" to "application/json")
    )
    Log.d("Api", "createOrderItem: ${response.body}")
    return response.statusCode == 200
}