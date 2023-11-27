package com.example.temi_beta.state

import androidx.compose.runtime.mutableStateOf

class TemiSocketStatus {
    val isConnect = mutableStateOf(true)
    val uri = mutableStateOf(mutableMapOf("ip" to "","port" to ""))
}