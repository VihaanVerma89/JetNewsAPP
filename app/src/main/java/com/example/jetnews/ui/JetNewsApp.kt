package com.example.jetnews.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetnews.data.AppContainer
import com.example.jetnews.ui.theme.JetnewsTheme
import kotlinx.coroutines.launch

@Composable
fun JetNewsApp(appContainer: AppContainer) {

    JetnewsTheme {

        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                    navigateToInterests = { navController.navigate(MainDestinations.INTERESTS_ROUTE) },
                    closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                )
            }
        ) {
            JetNewsNavGraph(appContainer = appContainer,
                navController = navController,
                scaffoldState = scaffoldState)
        }
    }
}