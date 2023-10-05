package com.example.temi_beta.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.temi_beta.component.cartitem.CartItem
import com.example.temi_beta.hook.FutureBuilder
import com.example.temi_beta.utils.dpTextUnit
import com.example.temi_beta.viewmodel.ShoppingCartViewModel
import kotlinx.coroutines.launch


@Composable
fun ShoppingCart(navController: NavController, viewModel: ShoppingCartViewModel) {
    val total = viewModel.total
    val snackbarHostState = remember { SnackbarHostState() }
    return Scaffold(snackbarHost = {
        Box(Modifier.fillMaxSize()) {
            SnackbarHost(modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState,
                snackbar = { snackbarData: SnackbarData ->
                    Snackbar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 70.dp, top = 20.dp, bottom = 20.dp, end = 70.dp),
                        backgroundColor = Color.DarkGray,
                    ) {
                        Text(
                            text = snackbarData.message,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.dpTextUnit,
                            color = Color.White
                        )
                    }
                })
        }


    }, topBar = {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Shopping Cart",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.width(250.dp)
                    )
                }
            },
            colors = topAppBarColors(
                Color(0xFF42A5F5)
            ),
        )
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            FutureBuilder(future = { viewModel.fetchOrderItemList() }) { snapshot ->
                if (snapshot.hasData()) {
                    viewModel.sumOfPrice()
                    viewModel.getNumberOfOrderItem()
                    when (snapshot.data.isEmpty()) {
                        true -> Text(
                            text = "ไม่มีรายการอาหาร",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 25.dpTextUnit
                        )

                        false -> LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 56.dp),
                            state = rememberLazyListState()
                        ) {
                            val cartItemModelList = snapshot.data
                            items(cartItemModelList) { it ->
                                CartItem(
                                    model = it,
                                    onDecrease = {
                                        if (it.quantity.toInt() > 1) {
                                            viewModel.viewModelScope.launch {
                                                viewModel.updateOverQuantity(
                                                    it.menuId,
                                                    it.quantity.toInt() - 1
                                                )
                                                viewModel.fetchOrderItemList()
                                            }
                                        }
                                    },
                                    onIncrease = {
                                        viewModel.viewModelScope.launch {
                                            viewModel.updateOverQuantity(
                                                it.menuId,
                                                it.quantity.toInt() + 1
                                            )
                                            viewModel.fetchOrderItemList()
                                        }
                                    },
                                    onDelete = {
                                        viewModel.viewModelScope.launch {
                                            val response = viewModel.deleteCartItem(it.id)
                                            if (response != null) {
                                                snackbarHostState.showSnackbar(message = "${it.menuName} $response")
                                            }
                                            viewModel.fetchOrderItemList()
                                        }
                                    })
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Yellow)
                    .height(56.dp)
                    .padding(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5F)
                ) {
                    Text(
                        "Total : ${total.floatValue} thb",
                        fontSize = 18.dpTextUnit,
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 10.dp),

                        )
                }
                Button(
                    onClick = {
                        viewModel.onPayPress()
                        navController.navigate("home")
                    },
                    shape = RectangleShape,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Pay",
                            fontSize = 16.dpTextUnit,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}