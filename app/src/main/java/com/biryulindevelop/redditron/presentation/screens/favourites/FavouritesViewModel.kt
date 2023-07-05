package com.biryulindevelop.redditron.presentation.screens.favourites

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.biryulindevelop.data.repository.PagingSource
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.domain.tools.Change
import com.biryulindevelop.domain.tools.ListTypes
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.presentation.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: SubredditsRemoteRepository
) : StateViewModel() {

    fun setQuery(position: Int, isTabSource: Boolean) =
        if (isTabSource) setSource(position) else setType(position)

    private fun setSource(position: Int) {
        query.source = if (position == FIRST_POSITION_INDEX) ALL else SAVED
        query.listing = when (query.source) {
            ALL -> if (query.listing == ListTypes.SAVED_POST) ListTypes.POST else ListTypes.SUBREDDIT
            SAVED -> if (query.listing == ListTypes.POST) ListTypes.SAVED_POST else ListTypes.SUBSCRIBED_SUBREDDIT
            else -> query.listing
        }
        thingFlow.value = Change(query)
    }

    private fun setType(position: Int) {
        query.listing = if (query.source == ALL) {
            if (position == FIRST_POSITION_INDEX) ListTypes.POST else ListTypes.SUBREDDIT
        } else {
            if (position == FIRST_POSITION_INDEX) ListTypes.SAVED_POST else ListTypes.SUBSCRIBED_SUBREDDIT
        }
        thingFlow.value = Change(query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    var thingList: Flow<PagingData<ListItem>> =
        thingFlow.asStateFlow().flatMapLatest { query ->
            getThingList(query.value.listing, query.value.source).flow
        }.cachedIn(CoroutineScope(Dispatchers.IO))

    private fun getThingList(listType: ListTypes, source: String?): Pager<String, ListItem> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
            pagingSourceFactory = { PagingSource(repository, source, listType) }
        )

    fun subscribe(subQuery: SubQuery) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.subscribeOnSubreddit(subQuery.action, subQuery.name)
        }
    }

    fun votePost(voteDirection: Int, postName: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.votePost(voteDirection, postName)
        }
    }

    fun savePost(postName: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.savePost(postName)
        }
    }

    fun unsavePost(postName: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.unsavePost(postName)
        }
    }

    companion object {
        private const val FIRST_POSITION_INDEX = 0
        private const val ALL = ""
        private const val SAVED = "saved"
        private const val PAGE_SIZE_SUBREDDITS = 10
    }
}