package com.example.jetnews.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.example.jetnews.data.AppContainer
import com.example.jetnews.ui.theme.JetnewsTheme

@Composable
fun JetNewsApp(appContainer: AppContainer) {

    JetnewsTheme() {

        Scaffold(
            drawerContent = {
                AppDrawer(
                    currentRoute = "",
                    navigateToHome = {},
                    navigateToInterests = {},
                    closeDrawer = {},
                )
            }
        ) {

        }
    }
}