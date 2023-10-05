package com.example.temi_beta.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.temi_beta.utils.dpTextUnit
import com.example.temi_beta.viewmodel.ControlPanelViewModel
import com.example.temi_beta.viewmodel.DeviceSpec

@Composable
fun TtsBox(viewModel: ControlPanelViewModel, focusManager: FocusManager) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    return Column(modifier = Modifier.fillMaxSize()) {
        TextField(modifier = Modifier.fillMaxWidth(), value = text, label = {
            Text("input for speak")
        }, onValueChange = { newText ->
            text = newText
        })
        Button(onClick = {
            viewModel.textToSpeech(text.text, true)
            text = TextFieldValue("")
            focusManager.clearFocus()
        }, Modifier.fillMaxWidth()) {
            Text(text = "Speak")
        }


    }
}

@Composable
fun LocationBox(viewModel: ControlPanelViewModel, focusManager: FocusManager) {

    var locationName by remember { mutableStateOf(TextFieldValue("")) }
    var location = viewModel.location
    return Column(
        Modifier.fillMaxSize()
    ) {
        TextField(modifier = Modifier.fillMaxWidth(), value = locationName, label = {
            Text("input for save location")
        }, onValueChange = { newText ->
            locationName = newText
        })
        Button(onClick = {
            viewModel.saveLocation(name = locationName.text)
            locationName = TextFieldValue("")
            focusManager.clearFocus()

        }, Modifier.fillMaxWidth()) {
            Text(text = "Save location")
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            repeat(location.value.size) { index ->
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Button(

                        onClick = {
                            viewModel.goto(location = location.value[index])
                        },

                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            viewModel.getLocations()[index],
                            modifier = Modifier.fillMaxWidth(0.7F),
                            textAlign = TextAlign.Center

                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteLocation(viewModel.getLocations()[index])
                        },
                        Modifier
                            .size(40.dp)
                            .fillMaxWidth(0.3F)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun PositionPoviderBox(viewModel: ControlPanelViewModel) {
    var posXValue by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var posYValue by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var yawValue by remember {
        mutableStateOf(TextFieldValue(""))
    }
    return Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            viewModel.repose()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Repose")
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(Modifier.padding(8.dp)) {
                TextField(
                    placeholder = {
                        Text(
                            "x-axis position in meters",
                            fontSize = 20.dpTextUnit,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    value = posXValue,
                    onValueChange = { it ->
                        posXValue = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = {
                        Text(
                            "y-axis position in meters",
                            fontSize = 20.dpTextUnit,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    value = posYValue,
                    onValueChange = { it ->
                        posYValue = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = {
                        Text(
                            "Yaw rotation in degrees, with respect to the coordinate frame.",
                            fontSize = 20.dpTextUnit,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    value = yawValue,
                    onValueChange = { it ->
                        yawValue = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
                Button(onClick = {
                    viewModel.goToPostion(
                        posX = posXValue.text.toFloat(),
                        posY = posYValue.text.toFloat(),
                        tilt = 55,
                        yaw = yawValue.text.toFloat()
                    )
                }) {
                    Text(
                        text = "Submit",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.dpTextUnit
                    )
                }
            }
        }
    }
}


@Composable
fun DetectionAndTrackingBox(viewModel: ControlPanelViewModel) {
    return Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Detection and Tracking",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Button({
            viewModel.detectionSwitch()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("set detection")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            viewModel.trackingSwitch()
        }, Modifier.fillMaxWidth()) {
            Text(text = "set tracking")
        }
    }
}


@Composable
fun TiltingBox(viewModel: ControlPanelViewModel) {
    var tiltValue by remember { mutableStateOf(TextFieldValue("")) }
    var speedValue by remember {
        mutableStateOf(TextFieldValue(""))
    }
    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            placeholder = {
                Text(
                    "This value ranges between -25 degrees (screen tilted fully downwards) to +55 degrees",
                    fontSize = 20.dpTextUnit,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            value = tiltValue,
            onValueChange = { it ->
                tiltValue = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            placeholder = {
                Text(
                    "Spedd value",
                    fontSize = 20.dpTextUnit,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            value = speedValue,
            onValueChange = { it ->
                speedValue = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        Button(onClick = {
            viewModel.tiltBy(tiltValue.text.toInt(), speedValue.text.toFloat())
        }, shape = RectangleShape) {
            Text(text = "Submit", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        }
    }

}

@Composable
fun Information(viewModel: ControlPanelViewModel) {
    val deviceSpecs = viewModel.deviceSpecs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Device Specifications",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(CenterHorizontally)
        )

        LazyColumn {
            items(deviceSpecs.size) { spec ->
                DeviceSpecItem(deviceSpecs[spec])
            }
        }
    }
}

@Composable
fun DeviceSpecItem(spec: DeviceSpec) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = spec.name, fontWeight = FontWeight.Bold)
        Text(text = spec.value, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun ControlPanel(
    navController: NavController, viewModel: ControlPanelViewModel
) {

    val focusManager = LocalFocusManager.current

    val topicBox: List<String> = listOf(
        "Text To Speech",
        "Location provider",
        "Position",
        "Detection and TrackIng",
        "Tilting",
        "Information"
    )

    var starterBox by remember {
        mutableStateOf(0)
    }

    return Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Control Panel", textAlign = TextAlign.Center, color = Color.White
            )
        },
            colors = TopAppBarDefaults.smallTopAppBarColors(Color(0xFF42A5F5)),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("home")
                }) {
                    Icon(Icons.Outlined.ArrowBack, "", Modifier.size(60.dp), tint = Color.White)
                }
            })
    }) { contentPadding ->
        Box(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Row(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.4F)
                        .verticalScroll(rememberScrollState())
                ) {
                    repeat(topicBox.size) { index ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = {
                                starterBox = index
                            }, modifier = Modifier.padding(10.dp)) {
                                Text(
                                    topicBox[index],
                                    fontSize = 20.dpTextUnit,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }


                    }

                }
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    when (starterBox) {
                        0 -> TtsBox(viewModel = viewModel, focusManager = focusManager)
                        1 -> LocationBox(viewModel = viewModel, focusManager = focusManager)
                        2 -> PositionPoviderBox(viewModel = viewModel)
                        3 -> DetectionAndTrackingBox(viewModel = viewModel)
                        4 -> TiltingBox(viewModel = viewModel)
                        5 -> Information(viewModel = viewModel)
                    }
                }
            }
        }
    }
}