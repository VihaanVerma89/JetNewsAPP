package com.example.jetnews.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetnews.R
import com.example.jetnews.data.posts.impl.PostsRepository
import com.example.jetnews.model.Post
import com.example.jetnews.ui.components.InsetAwareTopAppBar
import com.example.jetnews.ui.home.PostCardHistory
import com.example.jetnews.ui.home.PostCardPopular
import com.example.jetnews.ui.home.PostCardSimple
import com.example.jetnews.ui.home.PostCardTop
import com.example.jetnews.ui.state.UiState
import com.example.jetnews.utils.produceUiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

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
        openDrawer = openDrawer,
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
            InsetAwareTopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            openDrawer()
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_jetnews_logo),
                            contentDescription = stringResource(R.string.cd_open_navigation_drawer)
                        )
                    }
                }
            )
        }) {
        LoadingContent(empty = posts.initialLoad,
            emptyContent = { FullScreenLoading() },
            loading = posts.loading,
            onRefresh = onRefreshPosts,
            content = {
                HomeScreenErrorAndContent(posts = posts, navigateToArticle = navigateToArticle)
            }
        )
        FullScreenLoading()

    }
}

@Composable
private fun HomeScreenErrorAndContent(
    posts: UiState<List<Post>>,
    navigateToArticle: (postId: String) -> Unit,
) {
    if (posts.data != null) {
        PostList(posts.data, navigateToArticle = navigateToArticle, setOf(), {})
    }
}

@Composable
private fun PostList(
    posts: List<Post>,
    navigateToArticle: (postId: String) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val postTop = posts[3]
    val postsSimple = posts.subList(0, 2)
    val postsPopular = posts.subList(2, 7)
    val postsHistory = posts.subList(7, 10)

    LazyColumn() {
        item { PostListTopSection(postTop, navigateToArticle) }
        item { PostListSimpleSection(postsSimple, navigateToArticle, favorites, onToggleFavorite) }
        item { PostListPopularSection(postsPopular, navigateToArticle) }
        item { PostListHistorySection(postsHistory, navigateToArticle) }
    }
}


@Composable
private fun PostListHistorySection(
    postsHistory: List<Post>,
    navigateToArticle: (postId: String) -> Unit,
) {
    Column {
        postsHistory.forEach {
            PostCardHistory(post = it, navigateToArticle = navigateToArticle)
            PostListDivider()
        }
    }
}

@Composable
private fun PostListTopSection(post: Post, navigateToArticle: (postId: String) -> Unit) {
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        text = stringResource(id = R.string.home_top_section_title),
        style = MaterialTheme.typography.subtitle1
    )
    PostCardTop(
        post = post,
        modifier = Modifier.clickable(onClick = { navigateToArticle(post.id) })
    )
    PostListDivider()
}

@Composable
fun PostListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            content = content,
        )
    }
}

@Composable
fun PostListSimpleSection(
    posts: List<Post>,
    navigateToArticle: (postId: String) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
) {

    Column {
        posts.forEach { post ->
            PostCardSimple(post = post,
                navigateToArticle = navigateToArticle,
                isFavorite = favorites.contains(post.id),
                onToggleFavorite = { onToggleFavorite(post.id) })
        }

    }
}

@Composable
private fun PostListPopularSection(
    posts: List<Post>,
    navigateToArticle: (postId: String) -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.home_popular_section_title),
            style = MaterialTheme.typography.subtitle1
        )

        LazyRow(modifier = Modifier.padding(16.dp)) {
            items(posts) { post ->
                PostCardPopular(
                    post,
                    navigateToArticle,
                    Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        CircularProgressIndicator()
    }
}





