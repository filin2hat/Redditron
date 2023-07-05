package com.biryulindevelop.redditron.presentation.screens.profile

import androidx.lifecycle.viewModelScope
import com.biryulindevelop.common.constants.TOKEN_ENABLED
import com.biryulindevelop.common.constants.TOKEN_KEY
import com.biryulindevelop.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.storageservice.StorageService
import com.biryulindevelop.redditron.presentation.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repositoryProfile: ProfileRemoteRepository,
    private val repositorySubreddits: SubredditsRemoteRepository,
    private val storageService: StorageService
) : StateViewModel() {

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            loadState.value = LoadState.Loading
            loadState.value = LoadState.Content(repositoryProfile.getLoggedUserProfile())
        }
    }

    fun getClearedUrlAvatar(urlAvatar: String): String {
        return urlAvatar.substringBefore('?')
    }

    fun wipeToken() {
        storageService.save(TOKEN_KEY, "")
        storageService.save(TOKEN_ENABLED, false)
    }

    fun clearSaved() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repositorySubreddits.unsaveAllSavedPosts()
        }
    }
}