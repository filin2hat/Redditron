package com.biryulindevelop.redditron.application.di

import com.biryulindevelop.redditron.data.repository.*
import com.biryulindevelop.redditron.data.sharedprefsservice.SharedPrefsService
import com.biryulindevelop.redditron.domain.storageservice.StorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageServiceModule {

    @Singleton
    @Binds
    abstract fun bindStorageService(
        storageService: SharedPrefsService
    ): StorageService
}