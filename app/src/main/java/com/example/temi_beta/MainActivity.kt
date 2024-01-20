package com.example.temi_beta

//import androidx.compose.material3.ExperimentalMaterial3Api
import RobotProtocol
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.temi_beta.api.insertTable
import com.example.temi_beta.hook.DataStore
import com.example.temi_beta.hook.connectivityState
import com.example.temi_beta.state.LocationChangeHandler
import com.example.temi_beta.state.NumberOrder
import com.example.temi_beta.state.TemiSocketStatus
import com.example.temi_beta.utils.ConnectionState
import com.example.temi_beta.utils.TemiSocketIO
import com.example.temi_beta.utils.dpTextUnit
import com.example.temi_beta.view.Landing
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    val dataStore = DataStore()
    var robotProtocol: RobotProtocol =
        RobotProtocol(onGoToLocationStatusChangedListener = { instance,
                                                              location,
                                                              status,
                                                              descriptionId,
                                                              description ->
            val isLocationChange = dataStore.getValue<LocationChangeHandler>()?.state
            if (status == "complete") {
                isLocationChange?.value = false
//                instance.repose()
                runBlocking {
                    if (location != "home base") {
                        insertTable(location)
                    }

                }
            } else {
                isLocationChange?.value = true
            }
            socketIO.emit("receiver_moving_status", status)
        })

    var socketIO: TemiSocketIO = TemiSocketIO("192.168.137.1","5000",robotProtocol)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socketIO.emit("on_ready", "ready")
        dataStore.addValue(LocationChangeHandler())
        dataStore.addValue(NumberOrder())
        dataStore.addValue(TemiSocketStatus())
        dataStore.addValue(this)
        dataStore.addValue(socketIO)
        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        socketIO.connect()
        this.robotProtocol.onStart()
    }

    override fun onStop() {
        super.onStop()
        socketIO.disconnect()
        this.robotProtocol.onEnd()
    }
}

val selectedValue = mutableMapOf<String, Any>(
    "textColor" to Color.Black, "iconColor" to Color.Black, "isSelected" to true
)

val unSelectedValue = mutableMapOf<String, Any>(
    "textColor" to Color.White, "iconColor" to Color.White, "isSelected" to false
)


@Composable
fun App() {
    val dataStore = DataStore()
    val instance = dataStore.getValue<MainActivity>()
    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available
    val isLocationChange = dataStore.getValue<LocationChangeHandler>()?.state
    return MaterialTheme {
        when (isConnected) {
            true -> when (isLocationChange?.value) {
                false -> Landing(instance!!.robotProtocol, instance.socketIO)
                true -> Box(Modifier.fillMaxSize()) {
                    Text(
                        "กำลังเคลื่อนย้าย",
                        textAlign = TextAlign.Center,
                        fontSize = 20.dpTextUnit,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            "Error:= datastore is null",
                            textAlign = TextAlign.Center,
                            fontSize = 20.dpTextUnit,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            false -> Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                    Text(
                        "ไม่มีการเชื่อมต่ออินเทอร์เน็ต",
                        textAlign = TextAlign.Center,
                        fontSize = 20.dpTextUnit
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable()
fun AppPreview() {
    return App()
}