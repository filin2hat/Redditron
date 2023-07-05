package com.biryulindevelop.redditron.presentation.screens.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.domain.models.Friends
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentFriendsBinding
import com.biryulindevelop.redditron.presentation.delegates.friendsDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.fragment_friends) {
    private val binding by viewBinding(FragmentFriendsBinding::bind)
    private val viewModel: FriendsViewModel by viewModels()
    private val adapter by lazy { ListDelegationAdapter(friendsDelegate()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLoadingState()
        setToolbarBackButton()
    }

    private fun getLoadingState() {
        viewModel.getFriends()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state -> updateUi(state) }
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
                    loadContent(state.data as Friends)
                }

                is LoadState.Error -> {
                    common.progressBar.isVisible = false
                    common.error.isVisible = true
                    recyclerView.isVisible = false
                }
            }
        }
    }

    private fun loadContent(data: Friends) {
        adapter.items = data.friends_list
        binding.recyclerView.adapter = adapter
    }

    private fun setToolbarBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.navigateBack(this)
        }
    }
}