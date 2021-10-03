package com.example.jetnews.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.R
import com.example.jetnews.data.posts.impl.posts
import com.example.jetnews.model.Post


@Composable
fun PostCardTop(post: Post, modifier: Modifier = Modifier) {
    val typography = MaterialTheme.typography
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = post.imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = post.title,
            style = typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = post.metadata.author.name,
            style = typography.subtitle2,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = stringResource(
                id = R.string.home_post_min_read,
                formatArgs = arrayOf(
                    post.metadata.date,
                    post.metadata.readTimeMinutes
                )
            ),
            style = typography.subtitle2
        )
    }

}


@Preview
@Composable
fun previewPostCard() {
    val previewPosts = posts.subList(1, 2)
    val post = previewPosts[0]
    PostCardTop(post = post)
}