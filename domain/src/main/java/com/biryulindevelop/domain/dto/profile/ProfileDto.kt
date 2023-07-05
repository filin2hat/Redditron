package com.biryulindevelop.domain.dto.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ProfileDto(
    var name: String?,
    var id: String?,
    @Json(name = "icon_img")
    var urlAvatar: String?,

    @Json(name = "subreddit")
    var more_infos: UserDataSubDto?,
    var total_karma: Int?,
) {
    @JsonClass(generateAdapter = true)
    class UserDataSubDto(
        var subscribers: Int?,
        @Json(name = "title")
        var title: String?
    )
}
