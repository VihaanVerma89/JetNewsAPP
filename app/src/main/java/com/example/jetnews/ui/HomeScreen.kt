package com.example.jetnews.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.example.jetnews.R
import com.example.jetnews.data.posts.impl.PostsRepository
import com.example.jetnews.model.Post
import com.example.jetnews.ui.components.InsetAwareTopAppBar
import com.example.jetnews.ui.state.UiState
import com.example.jetnews.utils.produceUiState

@Composable
fun HomeScreen(
    postsRepository: PostsRepository,
    navigateToArticle: (String) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val (postUiState, refreshPost, clearError) = produceUiState(postsRepository) {
        getPosts()
    }

    HomeScreen(posts = postUiState.value,
        favorites = setOf(),
        onToggleFavorite = {},
        onRefreshPosts = { },
        onErrorDismiss = { },
        navigateToArticle = navigateToArticle,
        openDrawer = {

        },
        scaffoldState = scaffoldState)

}


@Composable
fun HomeScreen(
    posts: UiState<List<Post>>,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: () -> Unit,
    navigateToArticle: (String) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState,
) {

    val coroutineScope = rememberCoroutineScope()

    if (posts.hasError) {
        val errorMessage = stringResource(id = R.string.load_error)
        val retryMessage = stringResource(id = R.string.retry)
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            val title = stringResource(id = R.string.app_name)
            InsetAwareTopAppBar(title = { Text(text = title) })

        }) {

    }


}