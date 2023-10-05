package com.example.temi_beta.model

class MenuItemModel(val id:String,val name: String, val price: String, private val detail: String, val image: String) {
    companion object {
        fun fromJson(id:String,name: String, price: String, detail: String, image: String): MenuItemModel {
            return MenuItemModel(id,name, price, detail, image)
        }
    }

    fun toJson(): Map<String, String> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "detail" to detail,
            "image" to image
        )
    }
}