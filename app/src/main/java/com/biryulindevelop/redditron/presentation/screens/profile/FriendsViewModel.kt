package com.biryulindevelop.redditron.presentation.screens.profile

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.biryulindevelop.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.presentation.utils.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repository: ProfileRemoteRepository
) : StateViewModel() {

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
