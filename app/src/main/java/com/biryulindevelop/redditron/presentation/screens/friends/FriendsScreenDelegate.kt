package com.biryulindevelop.redditron.presentation.screens.friends

import com.biryulindevelop.domain.ListItem
import com.biryulindevelop.domain.models.Friends
import com.biryulindevelop.redditron.databinding.ItemFriendsBinding
import com.biryulindevelop.redditron.presentation.utils.loadImage
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun friendsDelegate() = adapterDelegateViewBinding<Friends.Friend, ListItem, ItemFriendsBinding>(
    { inflater, root -> ItemFriendsBinding.inflate(inflater, root, false) }
) {
    bind {
        with(binding) {
            name = item.name
            id = item.id
            friendPhoto.loadImage(item.avatar_url)
            executePendingBindings()
        }
    }
}
