package com.example.quotes.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.quotes.R
import com.example.quotes.presentation.viewmodel.DetailViewModel
import com.example.quotes.utilities.SafeCall

@SuppressLint("UnrememberedMutableState")
@Composable
fun AlbumDetail(
    detailViewModel: DetailViewModel = hiltViewModel(),
    artistName: String,
    albumName: String
) {
    SafeCall {
        detailViewModel.getAlbumDetail(artistName, albumName)
    }
    val album = detailViewModel.detailLiveData.observeAsState()

    val lazyListState = rememberLazyListState()
    var scrolledY = 0f
    var previousOffset = 0
    val isFavorite = mutableStateOf(album.value?.album?.isFavorite == true)
    val starColor = mutableStateOf(
        if (isFavorite.value) {
            Color.Yellow
        } else {
            Color.Black
        }
    )
    LazyColumn(
        Modifier.fillMaxSize(),
        lazyListState,
    ) {
        item {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(album.value?.album?.image?.last()?.text),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp), contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier
                            .height(95.dp)
                            .fillParentMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.6f))
                    )
                    var summery = ""
                    album.value?.album?.wiki?.summary?.split('.')?.take(3)?.forEach {
                        summery += it
                    }
                    Text(
                        color = Color.White, text = summery,
                        modifier = Modifier
                            .heightIn(95.dp, 95.dp)
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(end = 20.dp), contentAlignment = Alignment.BottomEnd
                ) {
                    Card(shape = RoundedCornerShape(50.dp), backgroundColor = Color.Gray) {
                        IconButton(
                            onClick = {
                                isFavorite.value = !isFavorite.value
                                album.value?.album?.isFavorite = isFavorite.value
                                if (isFavorite.value) {
                                    detailViewModel.saveAlbum(album.value?.album)
                                } else {
                                    detailViewModel.removeAlbum(
                                        album.value?.album?.name,
                                        album.value?.album?.artist
                                    )
                                }
                            },
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "",
                                tint = starColor.value,
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp)
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(R.string.songs),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        val track = album.value?.album?.tracks?.track
        items(track?.size ?: 0) { index ->
            Text(
                text = track?.get(index)?.name ?: "",
                Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp)
            )
        }
    }
}