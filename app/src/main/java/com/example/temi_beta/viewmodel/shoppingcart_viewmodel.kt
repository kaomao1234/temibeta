package com.example.temi_beta.viewmodel

import RobotProtocol
import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.temi_beta.api.OrderItem
import com.example.temi_beta.api.confirm_order
import com.example.temi_beta.api.deleteOrderItem
import com.example.temi_beta.api.getOrderItemList
import com.example.temi_beta.api.updateQuantity
import com.example.temi_beta.hook.DataStore
import com.example.temi_beta.model.CartItemModel
import com.example.temi_beta.state.LocationChangeHandler
import com.example.temi_beta.state.NumberOrder
import com.example.temi_beta.utils.TemiSocketIO
import kotlinx.coroutines.launch

class ShoppingCartViewModel (val robotProtocol: RobotProtocol?, private val temiSocketIO: TemiSocketIO) : ViewModel() {
    private var orderItemList = mutableStateOf<List<OrderItem>?>(null)
    private val cartItemList = mutableStateListOf<CartItemModel>()
    val total = mutableFloatStateOf(0F)
    val dataStore = DataStore()
    private val numOrderState = dataStore.getValue<NumberOrder>()?.state
    val locationChangeHandler = dataStore.getValue<LocationChangeHandler>()?.state
    init {
        Log.d("State",numOrderState.toString())
    }
    suspend fun fetchOrderItemList(): SnapshotStateList<CartItemModel> {
        val result = getOrderItemList()
        orderItemList.value = result
        cartItemList.clear()
        result?.map { value ->
            if (value.status == "0") {
                cartItemList.add(
                    CartItemModel.fromJson(
                        value.id.toString(),
                        value.menuId.toString(),
                        value.menuName,
                        value.quantity.toString(),
                        value.price.toString(),
                        value.subTotal.toString(),
                        value.image,
                        value.status
                    )
                )
            }
        }
        getNumberOfOrderItem()
        return cartItemList
    }

    fun getNumberOfOrderItem() {
        viewModelScope.launch {
            var totalOrderItem = 0
            getOrderItemList()?.map { it ->
                if (it.status == "0") {
                    totalOrderItem++
                }
            }
            numOrderState?.value =
                if (totalOrderItem > 99) 99 else totalOrderItem
        }
    }

    suspend fun updateOverQuantity(menuId: String, quantity: Int) {
        updateQuantity(menuId, quantity)
    }


    suspend fun deleteCartItem(itemId: String): String? {
        var detected: CartItemModel? = null
        cartItemList.map { it ->
            if (it.id == itemId) {
                detected = it
            }
        }
        if (detected != null) {
            cartItemList.remove(detected)
        }
        return deleteOrderItem(itemId)

    }

    fun onPayPress() {
        viewModelScope.launch {
            confirm_order("true")
            temiSocketIO.emit("on_ready", "ready")
        }
        cartItemList.clear()
    }

    fun sumOfPrice() {
        var value = 0F
        cartItemList.map { it ->
            value += it.subTotal.toFloat()
        }
        total.floatValue = value
    }
}