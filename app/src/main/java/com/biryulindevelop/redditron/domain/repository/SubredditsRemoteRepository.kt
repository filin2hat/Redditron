package com.biryulindevelop.redditron.domain.repository

import com.biryulindevelop.redditron.domain.ListItem
import com.biryulindevelop.redditron.domain.models.Subreddit
import com.biryulindevelop.redditron.domain.tools.ListTypes

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
