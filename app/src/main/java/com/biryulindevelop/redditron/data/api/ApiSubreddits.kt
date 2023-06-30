package com.biryulindevelop.redditron.data.api

import com.biryulindevelop.redditron.domain.dto.post.PostListingDto
import com.biryulindevelop.redditron.domain.dto.subreddit.SubredditDto
import com.biryulindevelop.redditron.domain.dto.subreddit.SubredditListingDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSubreddits {
    @GET("/subreddits/{source}")
    suspend fun getSubredditListing(
        @Path("source") source: String?,
        @Query("after") page: String
    ): SubredditListingDto

    @POST("/api/subscribe")
    suspend fun subscribeOnSubreddit(
        @Query("action") action: String,
        @Query("sr") name: String
    )

    @GET("/{source}")
    suspend fun getSubredditPosts(
        @Path("source") source: String?,
        @Query("after") page: String
    ): PostListingDto

    @GET("/{source}/about")
    suspend fun getSubredditInfo(
        @Path("source") source: String?
    ): SubredditDto

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribed(
        @Query("after") page: String?
    ): SubredditListingDto

    @GET("/subreddits/search")
    suspend fun searchSubredditsPaging(
        @Query("q") source: String?,
        @Query("after") page: String?
    ): SubredditListingDto
}
