package com.biryulindevelop.data.api

import com.biryulindevelop.domain.dto.post.SinglePostListingDto
import com.biryulindevelop.domain.dto.profile.ClickedUserProfileDto
import com.biryulindevelop.domain.dto.profile.FriendsListingDto
import com.biryulindevelop.domain.dto.profile.ProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiProfile {

    @GET("/api/v1/me")
    suspend fun getLoggedUserProfile(): ProfileDto

    @GET("/api/v1/me/friends")
    suspend fun getFriends(): FriendsListingDto

    @GET("/user/{username}/about")
    suspend fun getAnotherUserProfile(
        @Path("username") username: String
    ): ClickedUserProfileDto

    @PUT("/api/v1/me/friends/{username}")
    suspend fun makeFriends(
        @Path("username") username: String,
        @Body requestBody: String = "{\"name\": \"$username\"}"
    )

    @GET("/user/{username}")
    suspend fun getUserContent(
        @Path("username") username: String
    ): SinglePostListingDto
}