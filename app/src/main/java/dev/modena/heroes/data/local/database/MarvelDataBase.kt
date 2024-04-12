package dev.modena.heroes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.modena.heroes.data.local.dao.HeroDao
import dev.modena.heroes.data.local.entity.Hero

@Database(entities = [Hero::class], version = 1)
abstract class MarvelDataBase : RoomDatabase() {

    abstract val heroDao: HeroDao

}