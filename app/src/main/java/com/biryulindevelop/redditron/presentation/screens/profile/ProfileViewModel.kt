package com.biryulindevelop.redditron.presentation.screens.profile

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
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
        var clearedUrl = urlAvatar
        if (urlAvatar.contains('?')) {
            val questionMark = urlAvatar.indexOf('?', 0)
            clearedUrl = urlAvatar.substring(0, questionMark)
        }
        return clearedUrl
    }

    fun logout(fragment: Fragment) {
        storageService.save(com.biryulindevelop.common.constants.TOKEN_KEY, "")
        storageService.save(com.biryulindevelop.common.constants.TOKEN_ENABLED, false)
        fragment.findNavController()
            .navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationAuth())
    }

    fun navigateToFriends(fragment: Fragment) {
        fragment.findNavController().navigate(
            ProfileFragmentDirections.actionNavigationProfileToNavigationFriends()
        )
    }

    fun clearSaved() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repositorySubreddits.unsaveAllSavedPosts()
        }
    }
}