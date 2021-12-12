package com.example.jetnews.data.posts.impl

import com.example.jetnews.data.Result
import com.example.jetnews.model.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    suspend fun getPost(postId: String?): Result<Post>
    suspend fun getPosts(): Result<List<Post>>

    fun observeFavorites(): Flow<Set<String>>
}