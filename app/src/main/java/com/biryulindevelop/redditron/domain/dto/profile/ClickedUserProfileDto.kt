package com.biryulindevelop.redditron.domain.dto.profile

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClickedUserProfileDto(
    val data: ProfileDto,
    val kind: String
)