package com.example.temi_beta.component.loadimagefromurl

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

@Composable
fun LoadImageFromUrl(imageUrl: String,modifier: Modifier,contentScale: ContentScale) {
    val painter = rememberImagePainter(imageUrl)
    Image(
        painter = painter,
        contentDescription = null, // Set content description as per your requirement
        modifier = modifier,
        contentScale = contentScale
    )
}