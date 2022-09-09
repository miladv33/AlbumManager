package com.example.quotes.utilities

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * To ensure those list items cannot be clicked more than once
 *
 * @param isClicked To check if the function has been clicked
 * @param function
 * @receiver
 * @return
 */
@Composable
fun Modifier.SafeClick(isClicked: MutableState<Boolean>, function: () -> Unit): Modifier {
    val scope = rememberCoroutineScope()
    return clickable {
        // If the state is true, escape the function
        if (isClicked.value)
            return@clickable
        function.invoke()
        isClicked.value = true
        scope.launch(Dispatchers.Main) {
            delay(500)
            isClicked.value = false
        }
    }
}

/**
 * To ensure that function cannot call more than once
 *
 * @param function
 * @receiver
 */
@Composable
fun SafeCall(function: () -> Unit) {
    val fetched = remember { mutableStateOf(false) }
    if (!fetched.value) {
        fetched.value = true
        function.invoke()
    }
}