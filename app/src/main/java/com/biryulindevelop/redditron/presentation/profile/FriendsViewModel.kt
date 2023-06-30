package com.biryulindevelop.redditron.presentation.profile

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.biryulindevelop.redditron.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.redditron.domain.state.LoadState
import com.biryulindevelop.redditron.tools.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repository: ProfileRemoteRepository
) : BaseViewModel() {

    fun getFriends() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _state.value = LoadState.Loading
            _state.value = LoadState.Content(repository.getFriends())
        }
    }

    fun navigateBack(fragment: Fragment) {
        fragment.findNavController().popBackStack()
    }
}
