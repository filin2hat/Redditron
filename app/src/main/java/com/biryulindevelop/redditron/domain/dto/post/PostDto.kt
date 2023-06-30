package com.biryulindevelop.redditron.domain.dto.post

import com.biryulindevelop.redditron.domain.dto.ThingDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostDto(
    override val kind: String,
    val data: PostDataDto,
) : ThingDto {
    @JsonClass(generateAdapter = true)
    data class PostDataDto(
        val subreddit: String,
        val selftext: String?,
        val author_fullname: String,
        val saved: Boolean,
        val title: String,
        val subreddit_name_prefixed: String,
        val name: String,
        val score: Int,
        val thumbnail: String?,
        val post_hint: String?,
        val created: Double,
        val url_overridden_by_dest: String?,
        val subreddit_id: String,
        val id: String,
        val author: String,
        val num_comments: Int,
        val permalink: String,
        val url: String,
        val media: Media?,
        val is_video: Boolean,
        val likes: Boolean?,
    ) {
        @JsonClass(generateAdapter = true)
        data class Media(
            val reddit_video: RedditVideo?
        ) {
            @JsonClass(generateAdapter = true)
            data class RedditVideo(
                val bitrate_kbps: Int,
                val fallback_url: String,
                val height: Int,
                val width: Int,
                val scrubber_media_url: String,
                val dash_url: String,
                val duration: Int,
                val hls_url: String,
                val is_gif: Boolean,
                val transcoding_status: String
            )
        }
    }
}