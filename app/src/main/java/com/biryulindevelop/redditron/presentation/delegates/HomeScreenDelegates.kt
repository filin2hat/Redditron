package com.biryulindevelop.redditron.presentation.delegates

import android.view.View
import com.biryulindevelop.common.constants.SUBSCRIBE
import com.biryulindevelop.common.constants.UNSUBSCRIBE
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
                if (!subscribeButton.isSelected) UNSUBSCRIBE else SUBSCRIBE
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
            postBodyImage.visibility = if (item.postHint == "image") View.VISIBLE else View.GONE

            postBodyImage.apply {
                loadImage(item.url)
                visibility = if (item.postHint == "image") View.VISIBLE else View.GONE
            }

            downloadButton.setOnClickListener {
                val message = if (!downloadButton.isSelected) {
                    downloadButton.isSelected = true
                    getString(R.string.downloaded)
                } else {
                    getString(R.string.already_downloaded)
                }
                Snackbar.make(root, message, LENGTH_SHORT).show()
            }

            upVoteButton.isSelected = item.likedByUser == true
            upVoteButton.setOnClickListener {
                val voteDirection = if (!upVoteButton.isSelected) 1 else 0
                onClick(
                    SubQuery(voteDirection = voteDirection, name = item.name),
                    item,
                    ClickableView.VOTE
                )
                showScore(if (!upVoteButton.isSelected) item.score + 1 else item.score)
                upVoteButton.isSelected = !upVoteButton.isSelected
                downVoteButton.isSelected = false
            }

            downVoteButton.isSelected = item.likedByUser == false
            downVoteButton.setOnClickListener {
                val voteDirection = if (!downVoteButton.isSelected) -1 else 0
                onClick(
                    SubQuery(voteDirection = voteDirection, name = item.name),
                    item,
                    ClickableView.VOTE
                )
                showScore(if (!downVoteButton.isSelected) item.score - 1 else item.score)
                downVoteButton.isSelected = !downVoteButton.isSelected
                upVoteButton.isSelected = false
            }

            saveButton.isSelected = item.saved == true
            saveButton.setOnClickListener {
                val message = if (saveButton.isSelected) {
                    getString(R.string.unsaved)
                } else {
                    getString(R.string.saved)
                }
                Snackbar.make(root, message, LENGTH_SHORT).show()
                onClick(
                    SubQuery(name = item.name),
                    item,
                    if (saveButton.isSelected) ClickableView.UNSAVE else ClickableView.SAVE
                )
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
