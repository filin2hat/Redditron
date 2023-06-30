package com.biryulindevelop.redditron.data.api.authentication.interceptor

import android.content.Context
import com.biryulindevelop.redditron.application.TOKEN_KEY
import com.biryulindevelop.redditron.data.sharedprefsservice.SharedPrefsService

class AuthTokenProvider(
    private val context: Context,
    private val sharedPrefsService: SharedPrefsService
) {

    fun getToken() = sharedPrefsService.create(context)
        .getString(TOKEN_KEY, "")
}