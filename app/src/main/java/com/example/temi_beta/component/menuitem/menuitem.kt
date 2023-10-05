package com.example.temi_beta.component.menuitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.temi_beta.model.MenuItemModel
import com.example.temi_beta.utils.dpTextUnit


@Composable
fun MenuItem(model: MenuItemModel?, onClick: () -> Unit) {
    val _model: MenuItemModel;
    if (model == null) {
        _model = MenuItemModel.fromJson(
            id = "0",
            name = "Untitle",
            price = "None price",
            detail = "None",
            image = "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"
        )
    } else {
        _model = model
    }
    return Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 18.dp, hoveredElevation = 20.dp,
        ),
        onClick = onClick
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .height(250.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = _model.image, contentDescription = null, modifier = Modifier
                    .fillMaxSize(), contentScale = ContentScale.FillBounds
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
            ) {
                Column(
                    Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White).fillMaxWidth(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        _model.name,
                        fontSize = 20.dpTextUnit,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${_model.price} THB",
                        fontSize = 18.dpTextUnit,
                        color = Color.Black,
                        fontWeight = FontWeight.Light
                    )
                }
            }

        }

    }

}


