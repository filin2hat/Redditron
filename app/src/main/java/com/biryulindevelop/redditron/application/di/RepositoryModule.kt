package com.biryulindevelop.redditron.application.di

import com.biryulindevelop.redditron.data.repository.*
import com.biryulindevelop.redditron.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.redditron.domain.repository.SubredditsRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindProfileRemoteRepository(
        profileRemoteRepository: ProfileRemoteRepositoryImpl
    ): ProfileRemoteRepository

    @Singleton
    @Binds
    abstract fun bindSubredditsRemoteRepository(
        subredditsRemoteRepository: SubredditsRemoteRepositoryImpl
    ): SubredditsRemoteRepository
}

