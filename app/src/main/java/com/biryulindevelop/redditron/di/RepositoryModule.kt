package com.biryulindevelop.redditron.di

import com.biryulindevelop.data.repository.ProfileRemoteRepositoryImpl
import com.biryulindevelop.data.repository.SubredditsRemoteRepositoryImpl
import com.biryulindevelop.domain.repository.ProfileRemoteRepository
import com.biryulindevelop.domain.repository.SubredditsRemoteRepository
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

