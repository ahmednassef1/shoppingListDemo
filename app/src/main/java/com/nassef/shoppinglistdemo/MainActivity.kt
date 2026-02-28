package com.nassef.shoppinglistdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nassef.shoppinglistdemo.screens.shoppingList.MainScreen
import com.nassef.shoppinglistdemo.ui.theme.ShoppingListDemoTheme
import com.nassef.shoppinglistdemo.util.UiManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var uiManager: UiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListDemoTheme {
                ShoppingListApp(uiManager)
            }
        }
    }

    @Composable
    private fun ShoppingListApp(uiManager: UiManager) {
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        LaunchedEffect(snackbarHostState) {
            uiManager.snackbarMessage.collect {
                snackbarHostState.showSnackbar(
                    it,
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }
        }
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            /*Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )*/
            MainScreen()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShoppingListDemoTheme {
        Greeting("Android")
    }
}