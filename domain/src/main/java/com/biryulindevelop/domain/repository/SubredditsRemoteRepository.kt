package com.biryulindevelop.domain.repository

import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Subreddit
import com.biryulindevelop.domain.tools.ListTypes

interface SubredditsRemoteRepository {

    suspend fun getList(type: ListTypes, source: String?, page: String): List<ListItem>

    suspend fun subscribeOnSubreddit(action: String, name: String)

    suspend fun votePost(dir: Int, postName: String)

    suspend fun savePost(postName: String)

    suspend fun unsavePost(postName: String)

    suspend fun getSubredditInfo(name: String): Subreddit

    suspend fun getSinglePost(url: String): List<ListItem>

    suspend fun unsaveAllSavedPosts()
}
