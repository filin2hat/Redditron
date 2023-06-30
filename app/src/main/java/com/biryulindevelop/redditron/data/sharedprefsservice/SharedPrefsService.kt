package com.biryulindevelop.redditron.data.sharedprefsservice

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.biryulindevelop.redditron.application.TOKEN_ENABLED
import com.biryulindevelop.redditron.application.TOKEN_KEY
import com.biryulindevelop.redditron.application.TOKEN_NAME
import com.biryulindevelop.redditron.domain.storageservice.StorageService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsService @Inject constructor(@ApplicationContext context: Context) :
    StorageService {

    private val sharedPreferences = create(context)

    override fun save(key: String, data: Any?) {
        sharedPreferences.save(key, data)
    }

    override fun load(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun create(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            TOKEN_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun saveEncryptedToken(data: String) {
        save(TOKEN_ENABLED, true)
        return sharedPreferences.edit().putString(TOKEN_KEY, data).apply()
    }

    private fun SharedPreferences.save(key: String, value: Any?) {
        val edit = this.edit()
        if (value is String)
            edit.putString(key, value)
        if (value is Boolean)
            edit.putBoolean(key, value)
        edit.commit() || throw Exception("Could not save $value to SharedPreferences")
    }
}