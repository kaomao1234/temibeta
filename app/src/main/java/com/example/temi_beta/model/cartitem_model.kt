package com.example.temi_beta.model

class CartItemModel(
    val id: String,
    val menuId:String,
    val menuName: String,
    val quantity: String,
    val price: String,
    val subTotal:String,
    val image: String,
    val status:String,
) {
    companion object {
        fun fromJson(
            id: String,
            menuId: String,
            menuName: String,
            quantity: String,
            price: String,
            subTotal:String,
            image: String,
            status:String
        ): CartItemModel {
            return CartItemModel(id,menuId, menuName, quantity, price, subTotal, image,status)
        }
    }
    fun toJson(): Map<String, String> {
        return mapOf(
            "id" to id,
            "menuId" to menuId,
            "menuName" to menuName,
            "quantity" to quantity,
            "price" to price,
            "subTotal" to subTotal,
            "image" to image,
            "status" to status,

        )
    }
}