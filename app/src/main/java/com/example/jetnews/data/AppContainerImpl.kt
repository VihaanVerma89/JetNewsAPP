package com.example.jetnews.data

import com.example.jetnews.JetnewsApplication
import com.example.jetnews.data.posts.impl.FakePostsRepository
import com.example.jetnews.data.posts.impl.PostsRepository

interface AppContainer {
    val postsRepository: PostsRepository
}

class AppContainerImpl(jetnewsApplication: JetnewsApplication) : AppContainer {
    override val postsRepository: PostsRepository by lazy {
        FakePostsRepository()
    }
}