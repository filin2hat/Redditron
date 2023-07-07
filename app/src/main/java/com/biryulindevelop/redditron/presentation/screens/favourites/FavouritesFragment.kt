package com.biryulindevelop.redditron.presentation.screens.favourites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.common.constants.SUBSCRIBE
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Subreddit
import com.biryulindevelop.domain.tools.ClickableView
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentFavouritesBinding
import com.biryulindevelop.redditron.presentation.screens.home.HomePagedDataDelegationAdapter
import com.biryulindevelop.redditron.presentation.utils.setSelectedTabListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites) {
    private val binding by viewBinding(FragmentFavouritesBinding::bind)
    private val viewModel: FavouritesViewModel by viewModels()
    private val adapter by lazy {
        HomePagedDataDelegationAdapter { subQuery: SubQuery, item: ListItem, clickableView: ClickableView ->
            onClick(subQuery, item, clickableView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContent()
        tabLayoutSelectedListener(binding.toggleType, false)
        tabLayoutSelectedListener(binding.toggleSource, true)
        loadStateItemsObserve()
    }

    private fun loadContent() {
        binding.recyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.thingList.collect { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun loadStateItemsObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { state ->
                    with(binding) {
                        common.progressBar.isVisible =
                            state.refresh is LoadState.Loading
                        common.error.isVisible =
                            state.refresh is LoadState.Error || state.append is LoadState.Error || state.prepend is LoadState.Error
                        noSavedPosts.isVisible =
                            state.refresh is LoadState.NotLoading && adapter.itemCount == 0
                    }
                }
            }
        }
    }

    private fun tabLayoutSelectedListener(tabLayout: TabLayout, isSource: Boolean) {
        tabLayout.setSelectedTabListener { position ->
            viewModel.setQuery(position, isSource)
        }
    }

    private fun onClick(subQuery: SubQuery, item: ListItem, clickableView: ClickableView) {
        when (clickableView) {
            ClickableView.SAVE -> viewModel.savePost(postName = subQuery.name)
            ClickableView.UNSAVE -> viewModel.unsavePost(postName = subQuery.name)
            ClickableView.VOTE -> viewModel.votePost(
                voteDirection = subQuery.voteDirection,
                postName = subQuery.name
            )

            ClickableView.SUBSCRIBE -> {
                viewModel.subscribe(subQuery)
                val text = if (subQuery.action == SUBSCRIBE) {
                    getString(R.string.subscribed)
                } else {
                    getString(R.string.unsubscribed)
                }
                Snackbar.make(binding.recyclerView, text, BaseTransientBottomBar.LENGTH_SHORT)
                    .show()
            }

            ClickableView.USER -> findNavController().navigate(
                FavouritesFragmentDirections.actionNavigationFavouritesToNavigationUser(
                    subQuery.name
                )
            )

            ClickableView.SUBREDDIT -> findNavController().navigate(
                FavouritesFragmentDirections.actionNavigationFavouritesToNavigationSingleSubredditFragment(
                    (item as Subreddit).namePrefixed
                )
            )
        }
    }
}


