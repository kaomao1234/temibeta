package com.example.temi_beta.api

import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import fuel.HttpResponse
import fuel.httpDelete

suspend fun deleteOrderItem(itemId: String): String? {
    val body = mapOf<String, Any>(
        "ordered_item_id" to itemId

    )
    val jsonObject = JsonObject(body)
    val response: HttpResponse =
        "https://fastapideta-1-v4049125.deta.app/delete_order_item/{ordered_item_id}".httpDelete(

            body = jsonObject.toJsonString(),
            headers = mapOf("Content-Type" to "application/json")
        )
    Log.d("Api", "deleteOrderItem: ${response.body}")
    return if (response.statusCode == 200) {
        Klaxon().parse<Map<String, String>>(response.body)?.get("message")
    } else {
        null
    }
}
