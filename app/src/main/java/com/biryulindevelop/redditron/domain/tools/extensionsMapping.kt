package com.biryulindevelop.redditron.domain.tools

import com.biryulindevelop.redditron.domain.dto.comment.CommentDto
import com.biryulindevelop.redditron.domain.dto.comment.CommentListingDto
import com.biryulindevelop.redditron.domain.dto.post.PostDto
import com.biryulindevelop.redditron.domain.dto.profile.FriendsListingDto
import com.biryulindevelop.redditron.domain.dto.profile.ProfileDto
import com.biryulindevelop.redditron.domain.dto.profile.ProfileDto.UserDataSubDto
import com.biryulindevelop.redditron.domain.dto.subreddit.SubredditDto
import com.biryulindevelop.redditron.domain.models.Comment
import com.biryulindevelop.redditron.domain.models.Comments
import com.biryulindevelop.redditron.domain.models.Friend
import com.biryulindevelop.redditron.domain.models.Friends
import com.biryulindevelop.redditron.domain.models.Post
import com.biryulindevelop.redditron.domain.models.Profile
import com.biryulindevelop.redditron.domain.models.Subreddit
import com.biryulindevelop.redditron.domain.models.UserDataSubscribers


fun CommentListingDto.toCommentListing() = Comments(
    children = data.children.toListComment()
)

fun List<CommentListingDto>.toListCommentListing(): List<Comments> =
    this.map { item -> item.toCommentListing() }

fun List<CommentDto>.toListComment(): List<Comment> =
    this.map { item -> item.toComment() }

fun ProfileDto.toProfile() =
    Profile(
        name = name,
        id = id,
        urlAvatar = urlAvatar,
        more_infos = more_infos?.toUserDataSub(),
        total_karma = total_karma
    )

fun UserDataSubDto.toUserDataSub() = UserDataSubscribers(subscribers, title)

fun FriendsListingDto.FriendsDto.toFriends() = Friends(friends_list = children.toListFriends())

fun List<FriendsListingDto.FriendsDto.Children>.toListFriends(): List<Friend> =
    this.map { item -> item.toFriend() }

fun FriendsListingDto.FriendsDto.Children.toFriend() = Friend(id = id, name = name)

fun List<SubredditDto>.toListSubreddit(): List<Subreddit> = this.map { item -> item.toSubreddit() }

fun SubredditDto.toSubreddit(): Subreddit {

    var url = data.community_icon
    if (url != null) {
        if (url.contains('?')) {
            val questionMark = url.indexOf('?', 0)
            url = url.substring(0, questionMark)
        }
    } else url = data.icon_img

    return Subreddit(
        namePrefixed = data.display_name_prefixed,
        url = data.url,
        imageUrl = url,
        isUserSubscriber = data.user_is_subscriber,
        description = data.description,
        subscribers = data.subscribers,
        created = data.created,
        id = data.id,
        name = data.name
    )
}

fun List<PostDto>.toListPost(): List<Post> = this.map { item -> item.toPost() }

fun List<PostDto>.toListOfPostsNames(): List<String> = this.map { item -> item.toPostName() }

fun PostDto.toPostName(): String = data.name

fun PostDto.toPost(): Post {
    val voteDirection = when (data.likes) {
        null -> 0
        true -> 1
        false -> -1
    }
    return Post(
        selfText = data.selftext,
        authorFullname = data.author_fullname,
        saved = data.saved,
        title = data.title,
        subredditNamePrefixed = data.subreddit_name_prefixed,
        name = data.name,
        score = data.score,
        postHint = data.post_hint,
        created = data.created,
        id = data.id,
        author = data.author,
        numComments = data.num_comments,
        permalink = data.permalink,
        url = data.url,
        fallbackUrl = data.media?.reddit_video?.fallback_url,
        isVideo = data.is_video,
        likedByUser = data.likes,
        dir = voteDirection
    )
}
