package com.biryulindevelop.redditron.data.repository

import com.biryulindevelop.redditron.data.api.ApiPost
import com.biryulindevelop.redditron.data.api.ApiProfile
import com.biryulindevelop.redditron.data.api.ApiSubreddits
import com.biryulindevelop.redditron.data.api.dto.commentDto.CommentDto
import com.biryulindevelop.redditron.data.api.dto.postDto.PostDto
import com.biryulindevelop.redditron.domain.ListItem
import com.biryulindevelop.redditron.domain.models.Subreddit
import com.biryulindevelop.redditron.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.redditron.domain.tools.ListTypes
import com.biryulindevelop.redditron.domain.tools.toListOfPostsNames
import com.biryulindevelop.redditron.domain.tools.toListPost
import com.biryulindevelop.redditron.domain.tools.toListSubreddit
import com.biryulindevelop.redditron.domain.tools.toPost
import com.biryulindevelop.redditron.domain.tools.toSubreddit
import javax.inject.Inject

class SubredditsRemoteRepositoryImpl @Inject constructor(
    private val apiSubreddits: ApiSubreddits,
    private val apiPost: ApiPost,
    private val apiProfile: ApiProfile
) : SubredditsRemoteRepository {

    override suspend fun getList(type: ListTypes, source: String?, page: String): List<ListItem> {
        return when (type) {
            ListTypes.SUBREDDIT -> apiSubreddits.getSubredditListing(source, page)
                .data.children.toListSubreddit()

            ListTypes.SUBREDDIT_POST -> apiSubreddits.getSubredditPosts(source, page)
                .data.children.toListPost()

            ListTypes.POST -> apiPost.getPostListing(source, page).data.children.toListPost()

            ListTypes.SUBSCRIBED_SUBREDDIT -> apiSubreddits.getSubscribed(page)
                .data.children.toListSubreddit()

            ListTypes.SAVED_POST -> {
                val username = apiProfile.getLoggedUserProfile().name
                apiPost.getSaved(username, page).data.children.toListPost()
            }

            ListTypes.SUBREDDITS_SEARCH -> apiSubreddits.searchSubredditsPaging(
                source,
                page
            ).data.children.toListSubreddit()
        }
    }

    override suspend fun subscribeOnSubreddit(action: String, name: String) =
        apiSubreddits.subscribeOnSubreddit(action, name)

    override suspend fun votePost(dir: Int, postName: String) = apiPost.votePost(dir, postName)

    override suspend fun savePost(postName: String) = apiPost.savePost(postName)

    override suspend fun unsavePost(postName: String) = apiPost.unsavePost(postName)

    override suspend fun unsaveAllSavedPosts() {
        val username = apiProfile.getLoggedUserProfile().name
        val savedPostsNamesList: List<String> =
            apiPost.getAllSavedPosts(username).data.children.toListOfPostsNames()
        savedPostsNamesList.forEach { postName -> apiPost.unsavePost(postName) }
    }

    override suspend fun getSubredditInfo(name: String): Subreddit {
        return apiSubreddits.getSubredditInfo(name).toSubreddit()
    }

    override suspend fun getSinglePost(url: String): List<ListItem> {
        val list = mutableListOf<ListItem>()
        apiPost.getSinglePost(url).forEach { responseItem ->
            responseItem.data.children.forEach { child ->
                if (child is PostDto) list.add(child.toPost())
                else if (child is CommentDto) list.add(child.toComment())
            }
        }
        return list.toList()
    }
}