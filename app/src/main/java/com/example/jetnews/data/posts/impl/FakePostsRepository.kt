package com.example.jetnews.data.posts.impl

import com.example.jetnews.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import com.example.jetnews.data.Result

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
}