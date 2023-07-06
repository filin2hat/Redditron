package com.biryulindevelop.domain.tools

class Change<T>(val value: T)

data class Query(
    var listing: ListTypes = ListTypes.SUBREDDIT,
    var source: String = ""
)

data class FavoritesQuery(
    var listing: ListTypes = ListTypes.POST,
    var source: String = ""
)

data class SubQuery(
    var action: String = "",
    var name: String = "",
    var id: String = "",
    var voteDirection: Int = 0
)