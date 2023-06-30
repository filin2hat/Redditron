package com.biryulindevelop.redditron.presentation.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.biryulindevelop.redditron.databinding.FragmentFriendsBinding
import com.biryulindevelop.redditron.domain.models.Friends
import com.biryulindevelop.redditron.domain.state.LoadState
import com.biryulindevelop.redditron.presentation.delegates.friendsDelegate
import com.biryulindevelop.redditron.presentation.utils.BaseFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {

    override fun initBinding(inflater: LayoutInflater) = FragmentFriendsBinding.inflate(inflater)
    private val viewModel by viewModels<FriendsViewModel>()

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
        when (state) {
            LoadState.NotStartedYet -> {}
            LoadState.Loading -> {
                binding.common.progressBar.isVisible = true
                binding.common.error.isVisible = false
                binding.recyclerView.isVisible = false
            }

            is LoadState.Content -> {
                binding.common.progressBar.isVisible = false
                binding.common.error.isVisible = false
                binding.recyclerView.isVisible = true
                loadContent(state.data as Friends)
            }

            is LoadState.Error -> {
                binding.common.progressBar.isVisible = false
                binding.common.error.isVisible = true
                binding.recyclerView.isVisible = false
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