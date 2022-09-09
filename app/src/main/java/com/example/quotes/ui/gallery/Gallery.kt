package com.example.quotes.ui.gallery

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.quotes.R

@Composable
fun GalleryItem(imageAddress: String? = "", name: String? = "Nirvana") {
    Card(border = BorderStroke(3.dp, color = Color.Black)) {

        Image(
            painter = rememberAsyncImagePainter(imageAddress),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(128.dp)
                .background(Color.Blue)
        )
        val albumName = if (imageAddress == "") name else ""
        Box(modifier = Modifier.height(128.dp), contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = albumName ?: "",
                    modifier = Modifier.width(128.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
