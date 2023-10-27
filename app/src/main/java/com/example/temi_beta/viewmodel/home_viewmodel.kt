
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.temi_beta.api.createOrderItem
import com.example.temi_beta.api.getMenus
import com.example.temi_beta.api.getOrderItemList
import com.example.temi_beta.api.updateQuantity
import com.example.temi_beta.hook.DataStore
import com.example.temi_beta.model.MenuItemModel
import com.example.temi_beta.state.NumberOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val robotProtocol: RobotProtocol?) : ViewModel() {
    private var menus = mutableStateOf<List<List<Any>>?>(null)
    private var _search = MutableStateFlow(mutableStateOf(TextFieldValue("")))
    private var bufferMenuModels = mutableStateOf<MutableList<MenuItemModel>>(mutableListOf())
    var search = _search.asStateFlow()
    var menuModels = mutableStateOf<MutableList<MenuItemModel>>(mutableListOf())
    val dataStore = DataStore()
//    val numOrderState = DataStorePreference().getWithKey<MutableState<Int>>("_numOrderState")
    val numOrderState = dataStore.getValue<NumberOrder>()?.state
    init {
        Log.d("State",numOrderState.toString())
        viewModelScope.launch {
            getNumberOfOrderItem()
        }

    }

    suspend fun fetchMenus(): MutableState<MutableList<MenuItemModel>> {
        val fetchedMenus = getMenus()
        menus.value = fetchedMenus
        menusToMenusModel()
        return menuModels
    }

    private suspend fun getNumberOfOrderItem() {
        var totalOrderItem = 0
        val orderItemList = getOrderItemList()
        Log.d("API", "get orderItem: ${orderItemList}")
        getOrderItemList()?.map { it ->
            if (it.status == "0") {
                totalOrderItem++

            }
        }

        numOrderState?.value =
            if (totalOrderItem > 99) 99 else totalOrderItem

    }

    fun callCreateOrderItem(menuId: String, quantity: String) {
        viewModelScope.launch {
            val result = getOrderItemList()
            var itemIncluded = false
            result?.map { item ->
                if (item.menuId.toString() == menuId) {
                    updateQuantity(item.menuId.toString(), quantity.toInt() + item.quantity)
                    itemIncluded = true
                }
            }
            if (!itemIncluded) {
                createOrderItem(menuId, quantity)
            }
            getNumberOfOrderItem()
        }
    }

    private fun menusToMenusModel() {
        val newList = mutableListOf<MenuItemModel>()
        menus.value?.forEach { item ->
            val model = MenuItemModel.fromJson(
                id = item[0].toString(),
                name = item[1].toString(),
                price = item[4].toString(),
                detail = item[2].toString(),
                image = item[3].toString()
            )
            newList.add(model)

        }
        menuModels.value = newList.toMutableList()
        bufferMenuModels.value = newList.toMutableList()
    }

    fun filterMenuWithName(text: String) {
        Log.d(
            "HomeViewModel",
            "filterMenuWithName: menuModels: ${menuModels.value}\n bufferMenuModels:${bufferMenuModels.value}"
        )
        if (text.isEmpty()) {
            menuModels.value = bufferMenuModels.value
        } else {
            val filteredList =
                bufferMenuModels.value.filter { it.name.lowercase().startsWith(text.lowercase()) }
            menuModels.value = filteredList.toMutableList()
        }
    }

}

