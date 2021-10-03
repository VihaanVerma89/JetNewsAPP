package com.example.jetnews.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.R
import com.example.jetnews.data.posts.impl.post3
import com.example.jetnews.model.Post
import com.example.jetnews.ui.theme.JetnewsTheme


@Composable
fun AuthorAndReadTime(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Row(modifier)
    {
        Text(
            text = stringResource(
                id = R.string.home_post_min_read,
                formatArgs = arrayOf(
                    post.metadata.author.name,
                    post.metadata.readTimeMinutes
                )
            ),
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview("Post cards simple")
@Composable
fun PreviewPostCardsSimple() {
    JetnewsTheme() {
        Surface {
            PostCardSimple(post3, {}, false, {})
        }
    }
}

@Composable
fun PostCardSimple(
    post: Post,
    navigateToArticle: (String) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    val bookmarkAction = stringResource(if (isFavorite) R.string.unbookmark else R.string.bookmark)
    Row(
        modifier = Modifier
            .clickable(onClick = {
                navigateToArticle(post.id)
            })
            .padding(all = 16.dp)

    ) {
        PostImage(post = post, Modifier.padding(end = 16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            PostTitle(post = post)
            AuthorAndReadTime(post = post)
        }
        BookmarkButton(
            isBookmarked = isFavorite,
            onClick = onToggleFavorite,
            // Remove button semantics so action can be handled at row level
            modifier = Modifier.clearAndSetSemantics {}
        )
    }
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickLabel = stringResource(
        if (isBookmarked) R.string.unbookmark else R.string.bookmark
    )
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        IconToggleButton(
            checked = isBookmarked,
            onCheckedChange = { onClick() },
            modifier = modifier.semantics {
                // Use a custom click label that accessibility services can communicate to the user.
                // We only want to override the label, not the actual action, so for the action we pass null.
                this.onClick(label = clickLabel, action = null)
            }
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null // handled by click label of parent
            )
        }
    }
}


@Composable
fun PostTitle(post: Post) {
    Text(post.title, style = MaterialTheme.typography.subtitle1)
}

@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(post.imageThumbId),
        contentDescription = null, // decorative
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun PostCardHistory(post: Post, navigateToArticle: (String) -> Unit) {
    Row(
        Modifier
            .clickable(onClick = { navigateToArticle(post.id) })
    ) {
        PostImage(post, modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp))
        Column(Modifier
            .weight(1f)
            .padding(top = 16.dp, bottom = 16.dp)) {
            Text(text = stringResource(id = R.string.home_post_based_on_history),
                style = MaterialTheme.typography.overline)
            PostTitle(post)
            AuthorAndReadTime(post = post, Modifier.padding(top = 4.dp))
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.cd_more_actions)
                )
            }
        }
    }
}
