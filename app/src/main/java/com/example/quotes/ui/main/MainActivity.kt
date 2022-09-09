package com.example.quotes.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quotes.R
import com.example.quotes.presentation.base.BaseViewModel
import com.example.quotes.presentation.viewmodel.MainViewModel
import com.example.quotes.ui.detail.AlbumDetail
import com.example.quotes.ui.firstpage.firstPage
import com.example.quotes.ui.secondpage.SecondPage
import com.example.quotes.ui.topalbum.TopAlbum
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()
    private val homePage = "HomePage"
    private val searchPage = "Search"
    private val artistName = "ArtistName"
    private val albumName = "AlbumName"
    private val topAlbums = "TopAlbums/{$artistName}"
    private var albumDetail = "AlbumDetail/{$albumName}/{$artistName}"

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val title = remember { mutableStateOf("Last FM") }
            LaunchedEffect(navController) {
                navController.currentBackStackEntryFlow.collect { backStackEntry ->
                    // You can map the title based on the route using:
                    title.value = backStackEntry.destination.route?.split("/")?.first() ?: ""
                }
            }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = title.value)
                        },
                        navigationIcon =
                        {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                },
                content = {
                    Column {
                        NavHost(navController = navController, startDestination = homePage) {
                            composable(homePage) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colors.background
                                ) {
                                    firstPage(
                                        navigate = { navController.navigate(searchPage) },
                                        navigateToDetail = { savedArtistName, savedAlbumName ->
                                            navController.navigate(
                                                albumDetail.replace(
                                                    this@MainActivity.artistName,
                                                    savedArtistName
                                                )
                                                    .replace(albumName, savedAlbumName)
                                            )
                                        }
                                    )

                                }
                            }
                            composable(searchPage) {
                                SecondPage { searchedArtistName ->
                                    navController.navigate(
                                        topAlbums.replace(
                                            artistName,
                                            searchedArtistName
                                        )
                                    )
                                }
                            }
                            composable(
                                topAlbums,
                                arguments = listOf(navArgument(artistName) {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val artistName = backStackEntry.arguments?.getString(artistName)
                                    ?.replace("{", "")?.replace("}", "") ?: ""
                                TopAlbum(
                                    artistName = artistName,
                                    navigate = { searchedArtistName, searchedAlbumname ->
                                        navController.navigate(
                                            albumDetail.replace(
                                                this@MainActivity.artistName,
                                                searchedArtistName
                                            )
                                                .replace(albumName, searchedAlbumname)
                                        )

                                    }
                                )
                            }
                            composable(
                                albumDetail,
                                arguments = listOf(
                                    navArgument(artistName) { type = NavType.StringType },
                                    navArgument(albumName) { type = NavType.StringType })
                            ) { backStackEntry ->
                                val searchArtistName =
                                    backStackEntry.arguments?.getString(artistName)
                                        ?.replace("{", "")?.replace("}", "") ?: ""
                                val searchAlbumName =
                                    backStackEntry.arguments?.getString(albumName)?.replace("{", "")
                                        ?.replace("}", "") ?: ""
                                AlbumDetail(
                                    artistName = searchArtistName,
                                    albumName = searchAlbumName
                                )
                            }
                        }
                    }
                }
            )

        }
    }
}

/**
 * Dialog
 *
 * @param mainViewModel
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun Dialog(mainViewModel: BaseViewModel, message: String) {
    val openDialog = mainViewModel.showErrorDialogLiveData.observeAsState()
    if (openDialog.value == true) {
        AlertDialog(
            onDismissRequest = {
                mainViewModel.hideDialog()
            },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = message
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { false.also { mainViewModel.hideDialog() } }
                    ) {
                        Text(stringResource(R.string.dismiss))
                    }
                }
            }
        )
    }
}