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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.temi_beta.api.insertTable
import com.example.temi_beta.hook.DataStorePreference
import com.example.temi_beta.hook.connectivityState
import com.example.temi_beta.utils.ConnectionState
import com.example.temi_beta.utils.TemiSocketIO
import com.example.temi_beta.utils.dpTextUnit
import com.example.temi_beta.view.Landing
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    val dataStorePreference = DataStorePreference()
    var robotProtocol: RobotProtocol =
        RobotProtocol(onGoToLocationStatusChangedListener = { instance,
                                                              location,
                                                              status,
                                                              descriptionId,
                                                              description ->
            val isLocationChange =
                dataStorePreference.getWithKey<MutableState<Boolean>>("_isLocationChange")
            if (status == "complete") {
                isLocationChange.value = false
                instance.repose()
                runBlocking {
                    insertTable(location)
                }
            }else{
                isLocationChange.value = true
            }
            socketIO.emit("receiver_moving_status", status)
        })

    var socketIO: TemiSocketIO = TemiSocketIO("192.168.225.147", "5000", robotProtocol)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        socketIO.emit("response", "Hello world")
        setContent {
            App(this)
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
fun App(instance: MainActivity) {
    val connection by connectivityState()
    val isConnected = connection == ConnectionState.Available
    val isLocationChange =  remember { mutableStateOf(false) }
    instance.dataStorePreference.set("_isLocationChange",isLocationChange)
    return MaterialTheme {
        when (isConnected) {
            true -> when(isLocationChange.value){
                false -> Landing(instance.robotProtocol,instance.socketIO)
                true -> Box(Modifier.fillMaxSize()){
                    Text(
                        "Wait for locating",
                        textAlign = TextAlign.Center,
                        fontSize = 20.dpTextUnit,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            false -> Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                    Text(
                        "No internet connection",
                        textAlign = TextAlign.Center,
                        fontSize = 20.dpTextUnit
                    )
                }
            }
        }

    }
}

@Composable()
fun BApp() {
    return App(instance = MainActivity())
}


@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable()
fun AppPreview() {
    return BApp()
}