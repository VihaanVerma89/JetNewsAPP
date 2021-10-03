package com.example.jetnews.ui.components

import android.widget.ImageButton
import androidx.compose.material.*
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.R

@Composable
fun InsetAwareTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable() (() -> Unit)?,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
) {
    Surface(
        color = backgroundColor
    ) {
        TopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            backgroundColor = Color.Transparent,
            elevation = 1000.dp
        )
    }
}

@Preview
@Composable
fun previewTopAppBar() {
    InsetAwareTopAppBar(title = { Text("AppBar") },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.ic_jetnews_logo), null)
            }
        }
    )
}