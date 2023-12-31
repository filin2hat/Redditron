package com.biryulindevelop.domain.dto.profile

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendsListingDto(
    val kind: String,
    val data: FriendsDto
) {
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
}