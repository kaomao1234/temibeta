package com.example.temi_beta.api

import android.util.Log
import com.beust.klaxon.JsonObject
import fuel.HttpResponse
import fuel.httpPut

suspend fun updateQuantity(itemId: String, quantity: Int): Boolean {
    val body = mapOf<String, Any>(
        "item_id" to itemId,
        "quantity" to quantity
    )
    val response: HttpResponse =
        "https://fastapideta-1-v4049125.deta.app/update_ordered/{update_item_id}".httpPut(
            body = JsonObject(body).toJsonString(),
            headers = mapOf("Content-Type" to "application/json")
        )
    Log.d("Api", "updateQuantity: ${response.body}")
    return response.statusCode == 200
}