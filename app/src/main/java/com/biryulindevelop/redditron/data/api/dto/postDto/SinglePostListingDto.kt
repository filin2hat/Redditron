package com.biryulindevelop.redditron.data.api.dto.postDto

import com.biryulindevelop.redditron.data.api.dto.ThingDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SinglePostListingDto(
    val kind: String,
    val data: SinglePostListingDataDto
) {
    @JsonClass(generateAdapter = true)
    data class SinglePostListingDataDto(
        val children: List<ThingDto>
    )
}
