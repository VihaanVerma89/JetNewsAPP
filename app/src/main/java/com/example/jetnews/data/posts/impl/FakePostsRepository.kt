package com.example.jetnews.data.posts.impl

import com.example.jetnews.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import com.example.jetnews.data.Result
import com.example.jetnews.data.successOr
import kotlinx.coroutines.delay

class FakePostsRepository : PostsRepository {
    override suspend fun getPost(postId: String?): Result<Post> {
        return withContext(Dispatchers.IO)
        {
            val post = posts.find { it.id == postId }
            if (post == null) {
                Result.Error(IllegalArgumentException("Post not found"))
            } else {
                Result.Success(post)
            }
        }
    }

    override suspend fun getPosts(): Result<List<Post>> {
        return withContext(Dispatchers.IO)
        {
            delay(800)
            Result.Success(posts)
        }
    }
}