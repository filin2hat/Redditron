package com.biryulindevelop.redditron.presentation.screens.subreddit

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.biryulindevelop.data.repository.PagingSource
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.tools.ListTypes
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.presentation.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleSubredditViewModel @Inject constructor(
    private val repository: SubredditsRemoteRepository
) : StateViewModel() {

    fun getPostsList(name: String?): Flow<PagingData<ListItem>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSource(repository, name, ListTypes.SUBREDDIT_POST) }
    ).flow

    fun getSubredditInfo(name: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            loadState.value = LoadState.Loading
            loadState.value = LoadState.Content(repository.getSubredditInfo(name))
        }
    }

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
}
