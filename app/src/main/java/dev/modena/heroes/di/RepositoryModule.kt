package dev.modena.heroes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.modena.heroes.data.local.dao.HeroDao
import dev.modena.heroes.data.remote.RemoteMarvel
import dev.modena.heroes.repository.HeroRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providerHeroRepository(
        remoteMarvel: RemoteMarvel,
        heroDao: HeroDao
    ) = HeroRepository(remoteMarvel, heroDao)
}