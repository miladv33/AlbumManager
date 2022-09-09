package com.example.quotes.ui.secondpage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotes.data.model.artist.Artist
import com.example.quotes.presentation.viewmodel.SearchViewModel
import com.example.quotes.utilities.SafeClick

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SecondPage(
    searchViewModel: SearchViewModel = hiltViewModel(),
    navigate: (artistName: String) -> Unit,
) {
    val artistSearch = searchViewModel.artistSearchLiveData.observeAsState()
    val artistsWrapper = artistSearch.value
    val listState = rememberLazyListState()
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Search(searchViewModel)
        }
        val isClicked = remember {
            mutableStateOf(false)
        }
        LazyColumn(state = listState) {
            items(artistsWrapper?.size ?: 0) { index ->
                SearchItem(isClicked, artistsWrapper?.get(index)){
                    navigate.invoke(artistsWrapper?.get(index)?.name ?: searchViewModel.text)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), contentAlignment = Alignment.Center){
        if (listState.isScrolledToTheEnd() && !searchViewModel.fetchedMaximumResults()) {
            Loading()
            searchViewModel.startSearchArtist(searchViewModel.text, false)
        }
    }
}

@Composable
fun Search(searchViewModel: SearchViewModel) {
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                searchViewModel.startSearchArtist(searchViewModel.text, true)
            },
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    TextField(
        maxLines = 1,
        singleLine = true,
        value = searchViewModel.text,
        onValueChange = {
            searchViewModel.text = it
            searchViewModel.pageTitle?.value = "Searching for $it"
        },
        trailingIcon = trailingIconView,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SearchItem(isClicked: MutableState<Boolean>, artist: Artist?, function: () -> Unit) {
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .SafeClick(isClicked) {
                function.invoke()
            }) {
            Column(modifier = Modifier.weight(0.8f)) {
                Text(text = artist?.name ?: "")
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "listeners: ${artist?.listeners ?: ""}")
            }
            Column(
                modifier = Modifier.weight(0.1F),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Icon(Icons.Filled.ArrowForward, "")
            }
        }
        Box(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.Gray))

    }
}

@Composable
fun Loading() {
    CircularProgressIndicator(color = Color.Green, strokeWidth = 6.dp)
}

fun LazyListState.isScrolledToTheEnd(): Boolean {
    return layoutInfo.totalItemsCount > 1 && layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
}
