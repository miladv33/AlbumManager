package com.example.quotes.ui.firstpage

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotes.R
import com.example.quotes.data.model.album.Album
import com.example.quotes.presentation.viewmodel.HistoryViewModel
import com.example.quotes.ui.gallery.GalleryItem
import com.example.quotes.ui.main.Dialog
import com.example.quotes.utilities.SafeCall
import com.example.quotes.utilities.SafeClick

/**
 * Greeting
 *
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun firstPage(
    historyViewModel: HistoryViewModel = hiltViewModel(),
    navigateToDetail: (artistName: String, albumName: String) -> Unit,
    navigate: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(navigate)
        },
        content = {
            Column(modifier = Modifier.padding(10.dp)) {
                SafeCall{
                    historyViewModel.getAllSavedAlbum()
                }
                val history = historyViewModel.albumLiveData.observeAsState()
                SavedGallery( history.value, navigateToDetail)
            }
        }
    )
    Dialog(historyViewModel, stringResource(R.string.historyAlert))

}

@Composable
fun FloatingActionButton(navigate: () -> Unit) {
    //FAB custom color
    FloatingActionButton(
        onClick = { navigate.invoke() },
        backgroundColor = Color.Blue,
        contentColor = Color.White
    ){
        Icon(Icons.Filled.Search,"")
    }
}
@Composable
fun SavedGallery(
    list: List<Album>?,
    navigateToDetail: (artistName: String, albumName: String) -> Unit
) {
    val isClicked = remember {
        mutableStateOf(false)
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        list?.size?.let {
            items(it) { index ->
                Box(modifier = Modifier.SafeClick(isClicked) {
                    navigateToDetail.invoke(
                        list[index].artist ?: "",
                        list[index].name ?: ""
                    )
                }
                ) {
                    GalleryItem(imageAddress = list[index].image.last().text, list[index].name)
                }
            }
        }
    }
}

