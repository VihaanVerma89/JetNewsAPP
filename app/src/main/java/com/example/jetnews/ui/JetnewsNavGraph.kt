package com.example.jetnews.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetnews.data.AppContainer
import kotlinx.coroutines.launch

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val INTERESTS_ROUTE = "interests"
    const val ARTICLE_ROUTE = "post"
    const val ARTICLE_ID_KEY = "postId"
}

@Composable
fun JetNewsNavGraph(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE,
) {

    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = {
        coroutineScope.launch {
            scaffoldState.drawerState.open()
        }
    }

    NavHost(navController = navController, startDestination = startDestination)
    {
        composable(MainDestinations.HOME_ROUTE)
        {
            HomeScreen(
                postsRepository = appContainer.postsRepository,
                navigateToArticle = actions.navigateToArticle,
                openDrawer = openDrawer)
        }
    }
}


class MainActions(navController: NavHostController) {

    val navigateToArticle: (String) -> Unit = { postId: String ->
        navController.navigate("${MainDestinations.ARTICLE_ROUTE}/$postId")
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

}