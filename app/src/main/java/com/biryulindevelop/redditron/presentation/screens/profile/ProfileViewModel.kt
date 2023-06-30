package com.biryulindevelop.redditron.presentation.screens.profile

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.biryulindevelop.redditron.application.TOKEN_ENABLED
import com.biryulindevelop.redditron.application.TOKEN_KEY
import com.biryulindevelop.redditron.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.redditron.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.redditron.domain.state.LoadState
import com.biryulindevelop.redditron.domain.storageservice.StorageService
import com.biryulindevelop.redditron.presentation.profile.ProfileFragmentDirections
import com.biryulindevelop.redditron.presentation.utils.StateViewModel
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
            _state.value = LoadState.Loading
            _state.value = LoadState.Content(repositoryProfile.getLoggedUserProfile())
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
        storageService.save(TOKEN_KEY, "")
        storageService.save(TOKEN_ENABLED, false)
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