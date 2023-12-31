package com.biryulindevelop.redditron.presentation.screens.user

import androidx.lifecycle.viewModelScope
import com.biryulindevelop.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.presentation.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repositoryProfile: ProfileRemoteRepository,
    private val repositorySubreddits: SubredditsRemoteRepository
) : StateViewModel() {

    fun getProfileAndContent(name: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            loadState.value = LoadState.Loading
            val userProfile = repositoryProfile.getAnotherUserProfile(name)
            val userContent = repositoryProfile.getUserContent(name)
            loadState.value = LoadState.Content(userProfile, userContent)
        }
    }

    fun makeFriends(name: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repositoryProfile.makeFriends(name)
        }
    }

    fun votePost(voteDirection: Int, postName: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repositorySubreddits.votePost(voteDirection, postName)
        }
    }

    fun savePost(postName: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repositorySubreddits.savePost(postName)
        }
    }

    fun unsavePost(postName: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repositorySubreddits.unsavePost(postName)
        }
    }
}