package com.biryulindevelop.common.constants

import com.biryulindevelop.common.BuildConfig

const val REDIRECT_URI = "http://humblrmi/redirect"
const val CLIENT_ID = BuildConfig.CLIENT_ID

const val TOKEN_NAME = "name_token"
const val TOKEN_KEY = "key_token"
const val TOKEN_ENABLED = "token_enabled"
const val ONBOARDING_SHOWN = "onboarding_shown"

const val CLIENT_SECRET = ""
const val RESPONSE_TYPE = "code"
const val STATE = "my_state"
const val DURATION = "permanent"

const val SCOPE =
    "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits " +
            "privatemessages read report save submit subscribe vote wikiedit wikiread"

const val REQUEST =
    "https://www.reddit.com/api/v1/authorize.compact" +
            "?client_id=" +
            CLIENT_ID +
            "&response_type=" +
            RESPONSE_TYPE +
            "&state=" +
            STATE +
            "&redirect_uri=" +
            REDIRECT_URI +
            "&duration=" +
            DURATION +
            "&scope=" +
            SCOPE

const val SUBSCRIBE = "sub"
const val UNSUBSCRIBE = "unsub"



