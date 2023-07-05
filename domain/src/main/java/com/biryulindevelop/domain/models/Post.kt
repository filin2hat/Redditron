package com.biryulindevelop.domain.models

import com.biryulindevelop.domain.ListItem

data class Post(
    val selfText: String?,
    val authorFullname: String,
    val saved: Boolean,
    val title: String,
    val subredditNamePrefixed: String,
    override val name: String,
    val score: Int,
    val postHint: String?,
    val created: Double,
    override val id: String,
    val author: String,
    val numComments: Int,
    val permalink: String,
    val url: String,
    val fallbackUrl: String?,
    val isVideo: Boolean,
    val likedByUser: Boolean?,
    var dir: Int
) : ListItem
