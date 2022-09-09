package com.example.quotes.ui.topalbum

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotes.data.model.album.TopAlbum
import com.example.quotes.presentation.viewmodel.TopAlbumViewModel
import com.example.quotes.ui.gallery.GalleryItem
import com.example.quotes.ui.secondpage.Loading
import com.example.quotes.utilities.SafeCall
import com.example.quotes.utilities.SafeClick

@Composable
fun TopAlbum(
    topAlbumViewModel: TopAlbumViewModel = hiltViewModel(),
    artistName: String,
    navigate: (artistName: String, albumName: String) -> Unit
) {

    SafeCall {
        topAlbumViewModel.startGetTopAlbums(artistName, true)
    }
    topAlbumViewModel.artistName = artistName
    val topAlbums = topAlbumViewModel.topAlbumsLiveData.observeAsState()
    val list = topAlbums.value
    Column {
        TopAlbumGallery(list, topAlbumViewModel, navigate)
    }

}

@Composable
fun TopAlbumGallery(
    topAlbums: List<TopAlbum>?,
    topAlbumViewModel: TopAlbumViewModel,
    navigate: (artistName: String, albumName: String) -> Unit
) {
    val listState = rememberLazyGridState()
    val isClicked = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {

        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            topAlbums?.size?.let {
                items(it) { index ->
                    Box(modifier = Modifier.SafeClick(isClicked) {
                        navigate.invoke(
                            topAlbums[index].artist?.name ?: "",
                            topAlbums[index].name ?: ""
                        )
                    }) {
                        GalleryItem(
                            imageAddress =
                            topAlbums[index].image.first().text,
                            topAlbums[index].name
                        )
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), contentAlignment = Alignment.Center
        ) {
            if (listState.isScrolledToTheEnd() && !topAlbumViewModel.fetchedMaximumPages()) {
                Loading()
                topAlbumViewModel.startGetTopAlbums(topAlbumViewModel.artistName, false)
            }
        }
    }
}

fun LazyGridState.isScrolledToTheEnd(): Boolean {
    return layoutInfo.totalItemsCount > 1 && layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}
