package com.example.temi_beta.view

import HomeViewModel
import RobotProtocol
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.temi_beta.hook.DataStore
import com.example.temi_beta.selectedValue
import com.example.temi_beta.state.NumberOrder
import com.example.temi_beta.unSelectedValue
import com.example.temi_beta.utils.TemiSocketIO
import com.example.temi_beta.utils.dpTextUnit
import com.example.temi_beta.viewmodel.ControlPanelViewModel
import com.example.temi_beta.viewmodel.ShoppingCartViewModel


open class NavItem(
    val route: String, val icon: ImageVector, var value: MutableState<MutableMap<String, Any>>
) {
    object Home : NavItem(
        "home", Icons.Outlined.Home, mutableStateOf(
            selectedValue.toMutableMap()
        )
    )

    object ShoppingCart : NavItem(
        "shopping-cart", Icons.Outlined.ShoppingCart, mutableStateOf(
            unSelectedValue.toMutableMap()
        )
    )

    object ControlPanel : NavItem(
        "control-panel", Icons.Outlined.Settings, mutableStateOf(
            unSelectedValue.toMutableMap()
        )
    )
}
@Composable
fun Landing(robotProtocol: RobotProtocol,temiSocketIO: TemiSocketIO) {
    val context = LocalContext.current
    val dataStore = DataStore()
    val navController = rememberNavController()
    val items = listOf(
        NavItem.Home, NavItem.ShoppingCart
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val numOrderState = dataStore.getValue<NumberOrder>()?.state
    return Scaffold(Modifier.fillMaxSize(), bottomBar = {
        NavigationBar(
            containerColor = Color(0xFF00bcd4)
        ) {
            items.forEach { screen ->
                val isSelected: Boolean = screen.value.value["isSelected"] as Boolean
                NavigationBarItem(icon = {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .width(170.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BadgedBox(badge = {
                            if (screen.route == "shopping-cart") {
                                Text(
                                    text = numOrderState?.value.toString(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.dpTextUnit,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(
                                            Color.Red, CircleShape
                                        )
                                        .align(Alignment.Center),
                                    color = Color.White
                                )
                            }
                        }) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = null,
                                tint = screen.value.value["iconColor"] as Color,
                                modifier = Modifier.size(if (isSelected) 30.dp else 50.dp)
                            )
                        }

                        if (isSelected) {
                            Text(
                                screen.route,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.dpTextUnit,
                                color = screen.value.value["textColor"] as Color
                            )
                        }

                    }
                },
                    alwaysShowLabel = true,
                    selected = currentRoute == screen.route,
                    onClick = {
                        items.map { item ->
                            if (item.route != screen.route) {
                                item.value.value = unSelectedValue.toMutableMap()
                            } else {
                                item.value.value = selectedValue.toMutableMap()
                            }
                        }
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                        }
                    })
            }
        }
    }) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            NavHost(
                navController = navController,
                startDestination = "home",
                Modifier.fillMaxSize()
            ) {
                composable("home") {
                    Home(navController, remember { HomeViewModel(robotProtocol) })
                }
                composable("shopping-cart") {
                    ShoppingCart(navController,
                        remember { ShoppingCartViewModel(robotProtocol,temiSocketIO) })
                }
                composable("control-panel") {
                    ControlPanel(
                        navController = navController,
                        viewModel = remember { ControlPanelViewModel(robotProtocol) },
                    )
                }
            }
        }
    }
}