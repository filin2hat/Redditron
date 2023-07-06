package com.biryulindevelop.redditron.presentation.screens.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.common.constants.SUBSCRIBE
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Subreddit
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.tools.ClickableView
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentHomeBinding
import com.biryulindevelop.redditron.presentation.utils.setSelectedTabListener
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private val adapter by lazy {
        HomePagedDataDelegationAdapter { subQuery: SubQuery, item: ListItem, clickableView: ClickableView ->
            onClick(subQuery, item, clickableView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContent()
        getLoadingState()
        tabLayoutListener(binding.toggleSource)
        setSearchListener()
        loadStateItemsObserve()
    }

    private fun loadStateItemsObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { state ->
                    with(binding) {
                        common.progressBar.isVisible =
                            state.refresh is androidx.paging.LoadState.Loading || state.append is androidx.paging.LoadState.Loading
                        common.error.isVisible =
                            state.refresh is androidx.paging.LoadState.Error || state.append is androidx.paging.LoadState.Error || state.prepend is androidx.paging.LoadState.Error
                    }
                }
            }
        }
    }

    private fun loadContent() {
        binding.recyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.subredditsList.collect { pagingData -> adapter.submitData(pagingData) }
            }
        }
    }

    private fun getLoadingState() {
        viewModel.getSubreddits()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> updateUi(state) }
            }
        }
    }

    private fun updateUi(state: LoadState) {
        with(binding) {
            when (state) {
                LoadState.NotStartedYet -> {}
                LoadState.Loading -> {
                    common.progressBar.isVisible = true
                    common.error.isVisible = false
                    recyclerView.isVisible = false
                }

                is LoadState.Content -> {
                    common.progressBar.isVisible = false
                    common.error.isVisible = false
                    recyclerView.isVisible = true

                }

                is LoadState.Error -> {
                    common.progressBar.isVisible = false
                    common.error.isVisible = true
                    recyclerView.isVisible = false
                }
            }
        }
    }

    private fun tabLayoutListener(tabLayout: TabLayout) {
        tabLayout.setSelectedTabListener { position ->
            viewModel.setSource(position)
        }
    }

    private fun setSearchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) viewModel.onSearchButtonClick(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun onClick(subQuery: SubQuery, item: ListItem, clickableView: ClickableView) {
        when (clickableView) {
            ClickableView.SUBREDDIT -> findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToNavigationSingleSubredditFragment(
                    (item as Subreddit).namePrefixed
                )
            )

            ClickableView.SUBSCRIBE -> {
                viewModel.subscribe(subQuery)
                val text = getString(
                    if (subQuery.action == SUBSCRIBE) R.string.subscribed
                    else R.string.unsubscribed
                )
                Snackbar.make(binding.recyclerView, text, LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}


