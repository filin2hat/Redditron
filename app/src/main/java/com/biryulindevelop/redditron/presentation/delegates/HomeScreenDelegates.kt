package com.biryulindevelop.redditron.presentation.delegates

import android.view.View
import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Post
import com.biryulindevelop.domain.models.Subreddit
import com.biryulindevelop.domain.tools.ClickableView
import com.biryulindevelop.domain.tools.SubQuery
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.ItemPostImageBinding
import com.biryulindevelop.redditron.databinding.ItemSubredditBinding
import com.biryulindevelop.redditron.presentation.utils.loadCircleImage
import com.biryulindevelop.redditron.presentation.utils.loadImage
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun subredditsDelegate(
    onClick: (subQuery: SubQuery, item: ListItem, clickableView: ClickableView) -> Unit,
) = adapterDelegateViewBinding<Subreddit, ListItem, ItemSubredditBinding>(
    { inflater, root -> ItemSubredditBinding.inflate(inflater, root, false) }
) {
    with(binding) {
        bind {
            subredditTitle.text = item.namePrefixed
            subredditDescription.text = item.description
            subscribeButton.isSelected = item.isUserSubscriber == true
            if (item.imageUrl != null) subredditImage.loadCircleImage(item.imageUrl!!)
        }
        subscribeButton.setOnClickListener {
            subscribeButton.isSelected = !subscribeButton.isSelected
            val action =
                if (!subscribeButton.isSelected) com.biryulindevelop.common.constants.UNSUBSCRIBE else com.biryulindevelop.common.constants.SUBSCRIBE
            onClick(SubQuery(name = item.name, action = action), item, ClickableView.SUBSCRIBE)
        }

        fullSubredditCard.setOnClickListener {
            onClick(SubQuery(id = item.id), item, ClickableView.SUBREDDIT)
        }
    }
}

fun postsDelegate(
    onClick: (subQuery: SubQuery, item: ListItem, clickableView: ClickableView) -> Unit,
) = adapterDelegateViewBinding<Post, ListItem, ItemPostImageBinding>(
    { inflater, root -> ItemPostImageBinding.inflate(inflater, root, false) }
) {
    with(binding) {
        bind {
            showScore(item.score)
            postTitle.text = item.title
            subredditName.text = item.subredditNamePrefixed
            userName.text = context.getString(R.string.author, item.author)
            if (item.postHint == "image") {
                postBodyImage.apply {
                    loadImage(item.url)
                    visibility = View.VISIBLE
                }
            } else postBodyImage.visibility = View.GONE

            downloadButton.setOnClickListener {
                if (!downloadButton.isSelected) {
                    downloadButton.isSelected = true
                    Snackbar.make(root, getString(R.string.downloaded), LENGTH_SHORT).show()
                } else Snackbar.make(
                    root,
                    getString(R.string.already_downloaded),
                    LENGTH_SHORT
                )
                    .show()
            }

            upVoteButton.isSelected = item.likedByUser == true

            upVoteButton.setOnClickListener {
                if (!upVoteButton.isSelected) {
                    onClick(SubQuery(voteDirection = 1, name = item.name), item, ClickableView.VOTE)
                    showScore(item.score + 1)
                } else {
                    onClick(SubQuery(voteDirection = 0, name = item.name), item, ClickableView.VOTE)
                    showScore(item.score)
                }
                upVoteButton.isSelected = !upVoteButton.isSelected
                downVoteButton.isSelected = false
            }

            downVoteButton.isSelected = item.likedByUser == false
            downVoteButton.setOnClickListener {
                if (!downVoteButton.isSelected) {
                    onClick(
                        SubQuery(voteDirection = -1, name = item.name),
                        item,
                        ClickableView.VOTE
                    )
                    showScore(item.score - 1)
                } else {
                    onClick(SubQuery(voteDirection = 0, name = item.name), item, ClickableView.VOTE)
                    showScore(item.score)
                }
                downVoteButton.isSelected = !downVoteButton.isSelected
                upVoteButton.isSelected = false
            }

            saveButton.isSelected = item.saved == true
            saveButton.setOnClickListener {
                if (saveButton.isSelected) {
                    Snackbar.make(root, getString(R.string.unsaved), LENGTH_SHORT).show()
                    onClick(SubQuery(name = item.name), item, ClickableView.UNSAVE)
                } else {
                    Snackbar.make(root, getString(R.string.saved), LENGTH_SHORT)
                        .show()
                    onClick(SubQuery(name = item.name), item, ClickableView.SAVE)
                }
                saveButton.isSelected = !saveButton.isSelected
            }

            userName.setOnClickListener {
                onClick(SubQuery(name = item.author), item, ClickableView.USER)
            }
        }
    }
}

private fun AdapterDelegateViewBindingViewHolder<Post, ItemPostImageBinding>.showScore(score: Int) {
    if (score > 999_999) {
        binding.likes.text = getString(R.string.likesM, score / 1_000_000)
    } else {
        if (score > 999) binding.likes.text = getString(R.string.likesK, score / 1_000)
        else binding.likes.text = score.toString()
    }
}
