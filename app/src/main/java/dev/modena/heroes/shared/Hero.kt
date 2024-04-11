package dev.modena.heroes.shared

import android.util.Log
import dev.modena.marvel.model.ResponseMarvel

data class Hero(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnailURL: String
) {

    companion object {
        fun createByMarvel(marvel: ResponseMarvel): List<Hero> {
            return marvel.data.results.map { heroData ->
                //Log.d("URLThumbnail", heroData.thumbnail.getUrl())
                Hero(
                    id = heroData.id,
                    name = heroData.name,
                    description = heroData.description,
                    thumbnailURL = heroData.thumbnail.getUrl()
                )
            }
        }
    }
}
