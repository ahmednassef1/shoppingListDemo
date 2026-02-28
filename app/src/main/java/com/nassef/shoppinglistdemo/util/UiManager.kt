package com.nassef.shoppinglistdemo.util

import com.nassef.shoppinglistdemo.di.MainImmediateDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiManager @Inject constructor(@MainImmediateDispatcher private val dispatcher: CoroutineDispatcher){
    private val _snackbarMessage = Channel<String>()
    val snackbarMessage = _snackbarMessage.receiveAsFlow()

    suspend fun sendMessage(msg : String){
//        GlobalScope.launch(dispatcher) {
            _snackbarMessage.send(msg)
//        }
    }
}