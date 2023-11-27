package com.example.temi_beta.api

import android.util.Log
import com.beust.klaxon.JsonObject
import fuel.httpPost

suspend fun insertTable(tableName: String): Boolean {
    val body = mapOf<String, String>(
        "table_name" to tableName
    )
    val response =
        "https://fastapideta-1-v4049125.deta.app/insert_table/".httpPost(
            body = JsonObject(body).toJsonString(),
            headers = mapOf("Content-Type" to "application/json")
        )
    Log.d("API", "insertTable: ${response.body.toString()}")
    return response.statusCode == 200
}