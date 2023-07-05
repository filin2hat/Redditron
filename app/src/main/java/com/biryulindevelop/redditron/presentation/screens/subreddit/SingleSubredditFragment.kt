package com.biryulindevelop.redditron.presentation.screens.subreddit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.common.constants.SUBSCRIBE
import com.biryulindevelop.common.constants.UNSUBSCRIBE
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Subreddit
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.domain.tools.ClickableView
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentSingleSubredditBinding
import com.biryulindevelop.redditron.presentation.screens.home.HomePagedDataDelegationAdapter
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class SingleSubredditFragment : Fragment(R.layout.fragment_single_subreddit) {
    private val binding by viewBinding(FragmentSingleSubredditBinding::bind)
    private val viewModel: SingleSubredditViewModel by viewModels()
    private val args: SingleSubredditFragmentArgs by navArgs()
    private val adapter by lazy {
        HomePagedDataDelegationAdapter { subQuery: SubQuery, _: ListItem, clickableView: ClickableView ->
            onClick(subQuery, clickableView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel(args.name)
        setToolbarBackButton()
    }

    private fun observeViewModel(name: String) {
        binding.recycler.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getPostsList(args.name).collect { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
                launch {
                    viewModel.state.collect { state ->
                        updateUi(state)
                    }
                }
            }
        }
        viewModel.getSubredditInfo(name)
    }

    private fun updateUi(state: LoadState) {
        with(binding) {
            when (state) {
                LoadState.NotStartedYet -> {}
                LoadState.Loading -> {
                    recycler.isVisible = false
                    common.progressBar.isVisible = true
                    common.error.isVisible = false
                }

                is LoadState.Error -> {
                    recycler.isVisible = false
                    common.progressBar.isVisible = false
                    common.error.isVisible = true
                }

                is LoadState.Content -> {
                    recycler.isVisible = true
                    common.progressBar.isVisible = false
                    common.error.isVisible = false
                    val data = state.data as Subreddit
                    loadSubredditDescription(data)
                    setExpandButtonClick()
                    setShareButtonClick(data)
                    subscribeButton.isSelected = data.isUserSubscriber == true
                    setSubscribeButtonClick(data)
                }
            }
        }
    }

    private fun loadSubredditDescription(subreddit: Subreddit) {
        with(binding) {
            subredditName.text = subreddit.namePrefixed
            subscribers.text = getString(R.string.subscribers, subreddit.subscribers ?: 0)
            subredditDescription.text = subreddit.description
        }
    }

    private fun setExpandButtonClick() {
        with(binding) {
            expandButton.setOnClickListener {
                when (detailedInfo.visibility) {
                    View.GONE -> detailedInfo.visibility = View.VISIBLE
                    View.VISIBLE -> detailedInfo.visibility = View.GONE
                    View.INVISIBLE -> {}
                }
            }
        }
    }

    private fun setShareButtonClick(data: Subreddit) {
        binding.shareButton.setOnClickListener {
            shareLinkOnSubreddit(getString(R.string.share_url, data.url ?: ""))
        }
    }

    private fun setSubscribeButtonClick(data: Subreddit) {
        with(binding) {
            subscribeButton.setOnClickListener {
                subscribeButton.isSelected = !subscribeButton.isSelected
                val action = if (!subscribeButton.isSelected) UNSUBSCRIBE else SUBSCRIBE
                onClick(SubQuery(name = data.name, action = action), ClickableView.SUBSCRIBE)
            }
        }
    }

    private fun shareLinkOnSubreddit(url: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = getString(R.string.share_text)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)))
    }

    private fun onClick(subQuery: SubQuery, clickableView: ClickableView) {
        when (clickableView) {
            ClickableView.SAVE ->
                viewModel.savePost(postName = subQuery.name)

            ClickableView.UNSAVE ->
                viewModel.unsavePost(postName = subQuery.name)

            ClickableView.VOTE ->
                viewModel.votePost(voteDirection = subQuery.voteDirection, postName = subQuery.name)

            ClickableView.SUBSCRIBE -> {
                viewModel.subscribe(subQuery)
                val text =
                    if (subQuery.action == SUBSCRIBE) getString(
                        R.string.subscribed
                    )
                    else getString(R.string.unsubscribed)
                Snackbar.make(binding.recycler, text, LENGTH_SHORT).show()
            }

            ClickableView.USER -> findNavController().navigate(
                SingleSubredditFragmentDirections.actionNavigationSingleSubredditToNavigationUser(
                    subQuery.name
                )
            )

            else -> {}
        }
    }

    private fun setToolbarBackButton() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_single_subreddit_to_navigation_home
            )
        }
    }
}