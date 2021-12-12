package com.example.jetnews.data

import com.example.jetnews.JetnewsApplication
import com.example.jetnews.data.interests.InterestsRepository
import com.example.jetnews.data.interests.impl.FakeInterestsRepository
import com.example.jetnews.data.posts.impl.FakePostsRepository
import com.example.jetnews.data.posts.impl.PostsRepository

interface AppContainer {
    val postsRepository: PostsRepository
    val interestsRepository: InterestsRepository
}

class AppContainerImpl(jetnewsApplication: JetnewsApplication) : AppContainer {

    override val postsRepository: PostsRepository by lazy {
        FakePostsRepository()
    }

    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }
}
