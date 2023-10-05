package com.example.temi_beta.logic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ComposeListExample() {
    var mutableList: MutableState<MutableList<String>> = remember {
        mutableStateOf(mutableListOf())
    }
    var mutableList1: MutableState<MutableList<String>> = remember {
        mutableStateOf(mutableListOf())
    }
    var arrayList: MutableState<ArrayList<String>> = remember {
        mutableStateOf(ArrayList())
    }
    var arrayList1: MutableState<ArrayList<String>> = remember {
        mutableStateOf(ArrayList())
    }
    var list: MutableState<List<String>> = remember {
        mutableStateOf(listOf())
    }

    Column(
        Modifier.verticalScroll(state = rememberScrollState())
    ) {
        // Uncomment the below 5 methods one by one to understand how they work.
        // Don't uncomment multiple methods and check.


//        ShowListItems("MutableList", mutableList.value)
//         ShowListItems("Working MutableList", mutableList1.value)
        // ShowListItems("ArrayList", arrayList.value)
//         ShowListItems("Working ArrayList", arrayList1.value)
        ShowListItems("List", list.value)

        Button(
            onClick = {
                mutableList.value.add("")
                arrayList.value.add("")

                val newMutableList1 = mutableListOf<String>()
                mutableList1.value.forEach {
                    newMutableList1.add(it)
                }
                newMutableList1.add("")
                mutableList1.value = newMutableList1

                val newArrayList1 = arrayListOf<String>()
                arrayList1.value.forEach {
                    newArrayList1.add(it)
                }
                newArrayList1.add("")
                arrayList1.value = newArrayList1

                val newList = mutableListOf<String>()
                list.value.forEach {
                    newList.add(it)
                }
                newList.add("")
                list.value = newList
            },
        ) {
            Text(text = "Add")
        }
    }
}

@Composable
private fun ShowListItems(title: String, list: List<String>) {
    Text(title)
    Column {
        repeat(list.size) { index ->
            Button({}) {
                Text("$title -> $index Item Added")
            }
        }
    }
}