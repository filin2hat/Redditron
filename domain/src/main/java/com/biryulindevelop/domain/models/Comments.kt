package com.biryulindevelop.domain.models

import com.biryulindevelop.domain.ListItem

data class Comments(
    val children: List<Comment>
)

data class Comment(
    override val id: String,
    override val name: String,
    val likedByUser: Boolean?,
    val author: String,
    val score: Int?,
    val body: String?,
    val permalink: String?,
    val created: Double?,
    val linkId: String?,
    val subredditNamePrefixed: String?
) : ListItem
