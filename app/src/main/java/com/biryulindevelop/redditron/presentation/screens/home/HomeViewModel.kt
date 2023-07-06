package com.biryulindevelop.redditron.presentation.screens.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.biryulindevelop.data.repository.PagingSource
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.tools.ListTypes
import com.biryulindevelop.domain.tools.Query
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.presentation.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SubredditsRemoteRepository
) : StateViewModel() {
    private val _query = Query()
    private val subredditsSource get() = _query.source
    private val _subredditsSourceFlow = MutableStateFlow(subredditsSource)

    fun setSource(position: Int) {
        _query.source = if (position == FIRST_POSITION_INDEX) POPULAR else NEW
        _subredditsSourceFlow.value = subredditsSource
        getSubreddits()
    }

    fun getSubreddits() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            loadState.value = LoadState.Loading
            getSubredditsList(subredditsSource, ListTypes.SUBREDDIT)
            loadState.value = LoadState.Content()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    var subredditsList: Flow<PagingData<ListItem>> =
        _subredditsSourceFlow.asStateFlow()
            .flatMapLatest { source ->
                val listType = if (source == POPULAR || source == NEW) {
                    ListTypes.SUBREDDIT
                } else {
                    ListTypes.SUBREDDITS_SEARCH
                }
                getSubredditsList(source, listType).flow
            }
            .cachedIn(viewModelScope + Dispatchers.IO)


    private fun getSubredditsList(
        source: String?,
        listType: ListTypes
    ): Pager<String, ListItem> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
            pagingSourceFactory = { PagingSource(repository, source, listType) }
        )

    fun subscribe(subQuery: SubQuery) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.subscribeOnSubreddit(subQuery.action, subQuery.name)
        }
    }

    fun onSearchButtonClick(text: String) {
        viewModelScope.launch(Dispatchers.Main + handler) {
            loadState.value = LoadState.Loading
            _query.source = text
            _subredditsSourceFlow.value = subredditsSource
            getSubredditsList(text, ListTypes.SUBREDDITS_SEARCH)
            loadState.value = LoadState.Content()
        }
    }

    companion object {
        private const val FIRST_POSITION_INDEX = 0
        private const val NEW = "new"
        private const val POPULAR = ""
        private const val PAGE_SIZE_SUBREDDITS = 10
    }
}