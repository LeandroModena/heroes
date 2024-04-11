package dev.modena.heroes.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.modena.heroes.data.RemoteMarvel
import dev.modena.heroes.repository.HeroRepository
import dev.modena.heroes.shared.NetworkConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providerHeroRepository(remoteMarvel: RemoteMarvel) = HeroRepository(remoteMarvel)
}