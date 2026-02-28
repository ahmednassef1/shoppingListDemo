package com.nassef.shoppinglistdemo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nassef.shoppinglistdemo.ui.theme.TransParentGray

@Composable
fun ScreenWithLoadingDialog(isLoading: Boolean, content: @Composable (() -> Unit)) {
    Box {
        content()
        if (isLoading)
            Loading()
    }
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = TransParentGray),
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}