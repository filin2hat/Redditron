package com.biryulindevelop.domain.repository

import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Friends
import com.biryulindevelop.domain.models.Profile

interface ProfileRemoteRepository {

    suspend fun getLoggedUserProfile(): Profile

    suspend fun getFriends(): Friends

    suspend fun getAnotherUserProfile(username: String): Profile

    suspend fun makeFriends(username: String)

    suspend fun getUserContent(username: String): List<ListItem>
}