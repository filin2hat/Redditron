package com.biryulindevelop.redditron.di

import com.biryulindevelop.data.sharedprefsservice.SharedPrefsService
import com.biryulindevelop.domain.storageservice.StorageService
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