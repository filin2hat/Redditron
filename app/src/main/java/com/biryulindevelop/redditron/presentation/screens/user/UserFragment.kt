package com.biryulindevelop.redditron.presentation.screens.user

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Profile
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.tools.ClickableView
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentUserBinding
import com.biryulindevelop.redditron.presentation.screens.home.postsDelegate
import com.biryulindevelop.redditron.presentation.utils.loadImage
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {
    private val binding by viewBinding(FragmentUserBinding::bind)
    private val viewModel: UserViewModel by viewModels()
    private val args: UserFragmentArgs by navArgs()
    private val adapter by lazy {
        ListDelegationAdapter(postsDelegate { subQuery, _, clickableView ->
            onClick(subQuery, clickableView)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.buttonMakeFriends.setOnClickListener {
            viewModel.makeFriends(args.name)
            Snackbar.make(binding.containerView, getString(R.string.friends_now), LENGTH_SHORT)
                .show()
        }
        binding.recycler.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.getProfileAndContent(args.name)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> updateUi(state) }
            }
        }
    }

    private fun updateUi(state: LoadState) {
        with(binding) {
            containerView.isVisible = state is LoadState.Content
            common.progressBar.isVisible = state is LoadState.Loading
            common.error.isVisible = state is LoadState.Error

            if (state is LoadState.Content) {
                val data = state.data as Profile
                data.urlAvatar?.let { loadAvatar(it) }
                loadProfileTexts(data)
                loadUserContent(state.data2 as List<ListItem>)
            }
        }
    }

    private fun loadAvatar(url: String) {
        binding.avatarImg.loadImage(url)
    }

    private fun loadProfileTexts(data: Profile) {
        with(binding) {
            userName.text = data.name
            userId.text = getString(R.string.user_id, data.id)
            karma.text = getString(R.string.karma, data.total_karma ?: 0)
            followers.text = getString(R.string.followers, data.more_infos?.subscribers ?: "0")
        }
    }

    private fun loadUserContent(data: List<ListItem>) {
        adapter.items = data
    }

    private fun onClick(subQuery: SubQuery, clickableView: ClickableView) {
        when (clickableView) {
            ClickableView.SAVE -> viewModel.savePost(subQuery.name)
            ClickableView.UNSAVE -> viewModel.unsavePost(subQuery.name)
            ClickableView.VOTE -> viewModel.votePost(subQuery.voteDirection, subQuery.name)
            else -> {}
        }
    }
}
