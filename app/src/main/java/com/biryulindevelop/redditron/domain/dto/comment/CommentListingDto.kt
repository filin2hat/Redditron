package com.biryulindevelop.redditron.domain.dto.comment

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentListingDto(
    val kind: String,
    val data: CommentListingDataDto
) {
    @JsonClass(generateAdapter = true)
    data class CommentListingDataDto(
        val after: String?,
        val children: List<CommentDto>,
        val before: String?
    )
}