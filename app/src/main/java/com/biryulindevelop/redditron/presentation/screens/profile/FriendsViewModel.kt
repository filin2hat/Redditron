package com.biryulindevelop.redditron.presentation.screens.profile

import androidx.lifecycle.viewModelScope
import com.biryulindevelop.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.presentation.StateViewModel
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
            loadState.value = LoadState.Loading
            loadState.value = LoadState.Content(repository.getFriends())
        }
    }
}
