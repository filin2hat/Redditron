package com.biryulindevelop.redditron.domain.dto.post

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostListingDto(
    val kind: String,
    val data: PostListingDataDto
) {
    @JsonClass(generateAdapter = true)
    data class PostListingDataDto(
        val after: String?,
        val children: List<PostDto>,
        val before: Any?
    )
}
