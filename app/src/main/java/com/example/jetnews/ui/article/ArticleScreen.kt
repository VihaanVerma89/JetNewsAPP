package com.example.jetnews.ui.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetnews.R
import com.example.jetnews.data.posts.impl.PostsRepository
import com.example.jetnews.model.Post
import com.example.jetnews.ui.components.InsetAwareTopAppBar
import com.example.jetnews.ui.home.BookmarkButton
import com.example.jetnews.utils.produceUiState
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun ArticleScreen(
    postId: String?,
    postsRepository: PostsRepository,
    onBack: () -> Unit,
) {
    val (post) = produceUiState(producer = postsRepository, postId) {
        getPost(postId)
    }
    val postData = post.value.data ?: return
    val favorites by postsRepository.observeFavorites().collectAsState(initial = setOf())
    val contains = favorites.contains(postId)
    val rememberCoroutineScope = rememberCoroutineScope()

    ArticleScreen(post = postData, onBack = onBack, isFavorite = contains, onToggleFavorite = {

    })
}


@Composable
fun ArticleScreen(
    post: Post,
    onBack: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {

    Scaffold(
        topBar = {
            InsetAwareTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.article_published_in,
                            formatArgs = arrayOf(post.publication?.name.orEmpty())),
                        style = MaterialTheme.typography.subtitle2,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                post = post,
                onUnimplementedAction = { },
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite
            )
        }) {

        PostContent(post = post)

    }
}

@Composable
fun BottomBar(
    post: Post,
    onUnimplementedAction: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .navigationBarsPadding()
                .height(56.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = onUnimplementedAction) {
                Icon(
                    imageVector = Icons.Filled.ThumbUpOffAlt,
                    contentDescription = stringResource(R.string.cd_add_to_favorites)
                )
            }
            BookmarkButton(
                isBookmarked = isFavorite,
                onClick = onToggleFavorite
            )
            val context = LocalContext.current
            IconButton(onClick = { sharePost(post, context) }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.cd_share)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onUnimplementedAction) {
                Icon(
                    painter = painterResource(R.drawable.ic_text_settings),
                    contentDescription = stringResource(R.string.cd_text_settings)
                )
            }
        }
    }
}

/**
 * Show a share sheet for a post
 *
 * @param post to share
 * @param context Android context to show the share sheet in
 */
private fun sharePost(post: Post, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, post.title)
        putExtra(Intent.EXTRA_TEXT, post.url)
    }
    context.startActivity(Intent.createChooser(intent,
        context.getString(R.string.article_share_post)))
}
