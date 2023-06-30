package com.biryulindevelop.redditron.data.api.dto.profileDto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendsDto(
    val children: List<Children>
) {
    @JsonClass(generateAdapter = true)
    data class Children(
        val id: String,
        val name: String
    )
}
