package com.biryulindevelop.redditron.presentation.delegates

import com.biryulindevelop.redditron.databinding.ItemFriendsBinding
import com.biryulindevelop.redditron.domain.ListItem
import com.biryulindevelop.redditron.domain.models.Friend
import com.biryulindevelop.redditron.presentation.utils.loadImage
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun friendsDelegate() = adapterDelegateViewBinding<Friend, ListItem, ItemFriendsBinding>(
    { inflater, root -> ItemFriendsBinding.inflate(inflater, root, false) }
) {
    bind {
        binding.name = item.name
        binding.id = item.id
        binding.friendPhoto.loadImage(item.avatar_url)
        binding.executePendingBindings()
    }
}
