package com.biryulindevelop.redditron.presentation.screens.authorization

import androidx.lifecycle.viewModelScope
import com.biryulindevelop.data.api.authentication.ApiToken
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.storageservice.StorageService
import com.biryulindevelop.redditron.presentation.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiToken: ApiToken,
    private val storageService: StorageService
) : StateViewModel() {

    fun createToken(code: String) {
        if (code != PLUG)
            viewModelScope.launch(Dispatchers.IO) {
                loadState.value = LoadState.Loading
                try {
                    storageService.saveEncryptedToken(
                        apiToken.getToken(code = code).access_token
                    )
                    loadState.value = LoadState.Content()
                } catch (e: Exception) {
                    loadState.value = LoadState.Error(message = e.message.toString())
                }
            }
    }

    companion object {
        const val PLUG = ""
    }
}