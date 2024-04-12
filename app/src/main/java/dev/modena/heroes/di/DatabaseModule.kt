package dev.modena.heroes.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.modena.heroes.data.local.database.MarvelDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMarvelDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MarvelDataBase::class.java,
            "marvel_database"
        ).build()

    @Provides
    @Singleton
    fun provideHeroDao(marvelDataBase: MarvelDataBase) = marvelDataBase.heroDao

}