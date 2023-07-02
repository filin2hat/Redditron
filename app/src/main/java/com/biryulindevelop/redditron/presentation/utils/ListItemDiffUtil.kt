package com.biryulindevelop.redditron.presentation.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.biryulindevelop.domain.ListItem

class ListItemDiffUtil : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem == newItem
}