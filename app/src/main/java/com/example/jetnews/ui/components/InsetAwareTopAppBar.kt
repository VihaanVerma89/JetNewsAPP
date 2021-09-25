package com.example.jetnews.ui.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InsetAwareTopAppBar(
    title: @Composable () -> Unit,
) {
    TopAppBar(
        title = title
    )
}

@Preview
@Composable
fun previewTopAppBar() {
    TopAppBar(title = { Text("Vihaan app bar") })
}