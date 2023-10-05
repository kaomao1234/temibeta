package com.example.temi_beta.api

import fuel.httpPost

suspend fun insertTable(tableName: String): Boolean {
    val body = mapOf<String, String>(
        "table_name" to tableName
    )
    val response =
        "https://fastapideta-1-v4049125.deta.app/insert_table".httpPost(body = body.toString())
    return response.statusCode == 200
}