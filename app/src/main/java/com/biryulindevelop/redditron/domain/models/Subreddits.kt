package com.biryulindevelop.redditron.domain.models

import com.biryulindevelop.redditron.domain.ListItem

data class Subreddits(
    var subreddits_list: List<Subreddit>
)

data class Subreddit(
    val namePrefixed: String,
    val url: String?,
    val imageUrl: String?,
    val isUserSubscriber: Boolean?,
    val description: String?,
    val subscribers: Int?,
    val created: Double?,
    override val id: String,
    override val name: String
) : ListItem