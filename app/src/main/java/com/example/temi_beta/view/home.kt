package com.example.temi_beta.view

import HomeViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.temi_beta.component.menuitem.MenuItem
import com.example.temi_beta.hook.FutureBuilder
import com.example.temi_beta.model.MenuItemModel
import com.example.temi_beta.utils.dpTextUnit

@Composable
fun AlertingDialog(
    state: MutableState<Boolean>,
    menuModel: MenuItemModel,
    viewModel: HomeViewModel
) {
    val numOrderItem = remember { mutableIntStateOf(1) }
    return AlertDialog(onDismissRequest = {
    }, title = { Text(text = "Please enter the amount of food.") },
        text = {
            Column {
                Text(
                    "Would you like to add ${menuModel?.name} to your cart?",
                    fontSize = 25.dpTextUnit
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = {
                        if (numOrderItem.intValue > 1) numOrderItem.intValue-- else numOrderItem.intValue =
                            1
                    }) {
                        Box(Modifier.size(60.dp)) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center)
                            )
                        }

                    }
                    Text(
                        numOrderItem.intValue.toString(),
                        fontSize = 20.dpTextUnit,
                        textAlign = TextAlign.Center,
                    )
                    IconButton(onClick = {
                        if (numOrderItem.intValue < 20) {
                            numOrderItem.intValue++
                        } else numOrderItem.intValue =
                            20
                    }) {
                        Box(Modifier.size(60.dp)) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center)
                            )
                        }

                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    state.value = false
                    viewModel.callCreateOrderItem(menuModel.id, numOrderItem.intValue.toString())
                }) {
                Text("Confirm Button")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    state.value = false
                }) {
                Text("Dismiss Button")
            }
        })
}


@Composable
fun Home(navController: NavController, viewModel: HomeViewModel) {
    val searchValue = viewModel.search.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val selectedMenu = remember { mutableStateOf<MenuItemModel?>(null) }
    val openDialog = remember {
        mutableStateOf(false)
    }
    return Scaffold(topBar = {
        TopAppBar(title = {
            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp
                ), value = searchValue.value.value, onValueChange = { newText ->
                searchValue.value.value = newText
                viewModel.filterMenuWithName(newText.text)
            }, placeholder = {
                Text(
                    "Search Menu", fontSize = 20.dpTextUnit, color = Color.White
                )
            }, colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent, // Set your desired background color here
                unfocusedTextColor = Color.White, // Set your desired text color here
                unfocusedIndicatorColor = Color.White,
                focusedContainerColor = Color.Transparent
            )
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            Color(0xFF42A5F5)
        ), navigationIcon = {
            IconButton(modifier = Modifier.padding(start = 10.dp, end = 10.dp), onClick = {
                focusManager.clearFocus()
            }) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        })
    }) { contentPadding ->
        // Screen content
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            FutureBuilder(future = { viewModel.fetchMenus() }) { snapshot ->
                if (snapshot.hasData()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(snapshot.data.value.size) { index ->
                            val model: MenuItemModel = snapshot.data.value[index]
                            MenuItem(model = model, onClick = {
                                openDialog.value = true
                                selectedMenu.value = model
                            })
                        }
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
        if (openDialog.value) {
            AlertingDialog(state = openDialog, menuModel = selectedMenu.value!!, viewModel)
        }
    }
}


