package com.example.jetnews.data.posts.impl

import com.example.jetnews.data.Result
import com.example.jetnews.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class FakePostsRepository : PostsRepository {
    private val favorites = MutableStateFlow<Set<String>>(setOf())
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

    override fun observeFavorites(): Flow<Set<String>> = favorites


}