package com.example.temi_beta.api

import com.beust.klaxon.Klaxon
import fuel.httpGet

data class OrderItem(
    val id: Int,
    val menuId: Int,
    val menuName: String,
    val image: String,
    val price: Float,
    val quantity: Int,
    val subTotal: Float,
    val status:String,
)

suspend fun getOrderItemList(): List<OrderItem>? {
    val response =
        "https://fastapideta-1-v4049125.deta.app/order_list/".httpGet()
    if (response.statusCode == 200) {
        val jsonArray =
            Klaxon().parse<Map<String, List<Map<String, Any>>>>(response.body)?.get("order" +
                    "_list")
        val orderItemModelList = mutableListOf<OrderItem>()
        jsonArray?.map { it ->
            orderItemModelList.add(
                OrderItem(
                    it["id"].toString().toInt(),
                    it["menuId"].toString().toInt(),
                    it["menuName"].toString(),
                    it["image"].toString(),
                    it["price"].toString().toFloat(),
                    it["quantity"].toString().toInt(),
                    it["subtotal"].toString().toFloat(),
                    it["status"].toString()
                )
            )
        }
        return orderItemModelList
    } else {
        return null
    }
}

