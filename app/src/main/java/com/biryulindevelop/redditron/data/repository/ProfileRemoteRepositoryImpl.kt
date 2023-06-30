package com.biryulindevelop.redditron.data.repository

import com.biryulindevelop.redditron.data.api.ApiProfile
import com.biryulindevelop.redditron.domain.ListItem
import com.biryulindevelop.redditron.domain.dto.post.PostDto
import com.biryulindevelop.redditron.domain.models.Friends
import com.biryulindevelop.redditron.domain.models.Profile
import com.biryulindevelop.redditron.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.redditron.domain.tools.toFriends
import com.biryulindevelop.redditron.domain.tools.toPost
import com.biryulindevelop.redditron.domain.tools.toProfile
import javax.inject.Inject

class ProfileRemoteRepositoryImpl @Inject constructor(private val apiProfile: ApiProfile) :
    ProfileRemoteRepository {

    override suspend fun getLoggedUserProfile(): Profile =
        apiProfile.getLoggedUserProfile().toProfile()

    override suspend fun getFriends(): Friends = apiProfile.getFriends().data.toFriends()

    override suspend fun getAnotherUserProfile(username: String): Profile =
        apiProfile.getAnotherUserProfile(username).data.toProfile()

    override suspend fun makeFriends(username: String) = apiProfile.makeFriends(username)

    override suspend fun getUserContent(username: String): List<ListItem> {
        val list = mutableListOf<ListItem>()
        apiProfile.getUserContent(username).data.children.forEach { child ->
            if (child is PostDto) list.add(child.toPost())
        }
        return list.toList()
    }
}