package com.biryulindevelop.redditron.presentation.screens.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.biryulindevelop.redditron.data.repository.PagingSource
import com.biryulindevelop.redditron.domain.ListItem
import com.biryulindevelop.redditron.domain.repository.SubredditsRemoteRepository
import com.biryulindevelop.redditron.domain.state.LoadState
import com.biryulindevelop.redditron.domain.tools.ListTypes
import com.biryulindevelop.redditron.domain.tools.SubQuery
import com.biryulindevelop.redditron.presentation.utils.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleSubredditViewModel @Inject constructor(private val repository: SubredditsRemoteRepository) :
    StateViewModel() {

    fun getPostsList(name: String?): Flow<PagingData<ListItem>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSource(repository, name, ListTypes.SUBREDDIT_POST) }
    ).flow

    fun getSubredditInfo(name: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _state.value = LoadState.Loading
            _state.value = LoadState.Content(repository.getSubredditInfo(name))
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

    fun navigateToUser(fragment: Fragment, name: String) {
        fragment.findNavController().navigate(
            SingleSubredditFragmentDirections.actionNavigationSingleSubredditToNavigationUser(name)
        )
    }

    fun navigateBack(fragment: Fragment) {
        fragment.findNavController().navigate(
            SingleSubredditFragmentDirections.actionNavigationSingleSubredditToNavigationHome()
        )
    }

}