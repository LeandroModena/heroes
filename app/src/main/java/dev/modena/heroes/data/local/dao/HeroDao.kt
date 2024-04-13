package dev.modena.heroes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.modena.heroes.data.local.entity.Hero

@Dao
interface HeroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hero: Hero)

    @Delete
    suspend fun delete(hero: Hero)

    @Query("SELECT id FROM Hero")
    suspend fun getAllHeroIds(): List<Long>

    @Query("SELECT * FROM Hero ORDER BY name ASC LIMIT 10")
    suspend fun getHeroesSortedByName(): List<Hero>

    @Query("SELECT * FROM Hero ORDER BY name ASC LIMIT 10 OFFSET :offset")
    suspend fun getHeroesByPage(offset: Int): List<Hero>

}