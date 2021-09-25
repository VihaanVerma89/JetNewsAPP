package com.example.jetnews.data.posts.impl

import com.example.jetnews.model.Post
import com.example.jetnews.data.Result

interface PostsRepository {
    suspend fun getPost(postId: String?): Result<Post>
}