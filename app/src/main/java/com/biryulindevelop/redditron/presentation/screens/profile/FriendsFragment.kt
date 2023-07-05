package com.biryulindevelop.redditron.presentation.screens.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.domain.models.Friends
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentFriendsBinding
import com.biryulindevelop.redditron.presentation.delegates.friendsDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.fragment_friends) {
    private val binding by viewBinding(FragmentFriendsBinding::bind)
    private val viewModel: FriendsViewModel by viewModels()
    private val adapter by lazy { ListDelegationAdapter(friendsDelegate()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.recyclerView.adapter = adapter
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.getFriends()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> updateUi(state) }
            }
        }
    }

    private fun updateUi(state: LoadState) {
        with(binding) {
            common.progressBar.isVisible = state is LoadState.Loading
            common.error.isVisible = state is LoadState.Error
            recyclerView.isVisible = state is LoadState.Content
            if (state is LoadState.Content) {
                val data = state.data as Friends
                loadContent(data)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadContent(data: Friends) {
        adapter.items = data.friends_list
        adapter.notifyDataSetChanged()
    }
}