package com.biryulindevelop.redditron.presentation.utils

import androidx.lifecycle.ViewModel
import com.biryulindevelop.domain.state.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateViewModel : ViewModel() {

    protected val _state = MutableStateFlow<LoadState>(LoadState.NotStartedYet)
    val state = _state.asStateFlow()

    protected val handler = CoroutineExceptionHandler { _, e ->
        _state.value = LoadState.Error("${e.message}")
    }
}
