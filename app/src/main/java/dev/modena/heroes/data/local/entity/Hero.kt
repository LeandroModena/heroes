package dev.modena.heroes.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.modena.marvel.model.ResponseMarvel

@Entity
data class Hero(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String,
    val thumbnailURL: String,
    @Ignore
    var isFavorite: Boolean = true
) {

    constructor(
        id: Long,
        name: String,
        description: String,
        thumbnailURL: String,
    ) : this(id, name, description, thumbnailURL, true)

    companion object {
        fun createByMarvel(marvel: ResponseMarvel, ids: List<Long>): List<Hero> {
            return marvel.data.results.map { heroData ->
                Hero(
                    id = heroData.id,
                    name = heroData.name,
                    description = heroData.description,
                    thumbnailURL = heroData.thumbnail.getUrl(),
                    isFavorite = ids.contains(heroData.id)
                )
            }
        }
    }

}
