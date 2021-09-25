package com.example.jetnews.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.jetnews.data.AppContainer
import com.example.jetnews.ui.theme.JetnewsTheme

@Composable
fun JetNewsApp(appContainer: AppContainer) {

    JetnewsTheme {

        val navController = rememberNavController()

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
            JetNewsNavGraph(appContainer = appContainer, navController = navController)
        }
    }
}