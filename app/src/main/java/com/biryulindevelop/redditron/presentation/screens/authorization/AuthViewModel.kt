package com.biryulindevelop.redditron.presentation.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biryulindevelop.redditron.data.api.authentication.ApiToken
import com.biryulindevelop.redditron.domain.state.LoadState
import com.biryulindevelop.redditron.domain.storageservice.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiToken: ApiToken,
    private val storageService: StorageService
) : ViewModel() {

    private val _state = MutableStateFlow<LoadState>(LoadState.NotStartedYet)
    val state = _state.asStateFlow()

    fun createToken(code: String) {
        if (code != PLUG)
            viewModelScope.launch(Dispatchers.IO) {
                _state.value = LoadState.Loading
                try {
                    storageService.saveEncryptedToken(
                        apiToken.getToken(code = code).access_token
                    )
                    _state.value = LoadState.Content()
                } catch (e: Exception) {
                    _state.value = LoadState.Error(message = e.message.toString())
                }
            }
    }

    companion object {
        const val PLUG = ""
    }
}