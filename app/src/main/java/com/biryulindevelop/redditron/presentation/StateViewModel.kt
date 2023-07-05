package com.biryulindevelop.redditron.presentation

import androidx.lifecycle.ViewModel
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.tools.Change
import com.biryulindevelop.domain.tools.FavoritesQuery
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateViewModel : ViewModel() {

    protected val loadState = MutableStateFlow<LoadState>(LoadState.NotStartedYet)
    val state = loadState.asStateFlow()

    protected val handler = CoroutineExceptionHandler { _, e ->
        loadState.value = LoadState.Error("${e.message}")
    }

    protected val query = FavoritesQuery()
    protected val thingFlow = MutableStateFlow(Change(query))

}
