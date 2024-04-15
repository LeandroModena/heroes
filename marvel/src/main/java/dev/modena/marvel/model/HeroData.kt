package dev.modena.marvel.model

data class HeroData(
    val id: Long,
    val name: String,
    val description: String,
    val modified: String,
    val thumbnail: Thumbnail
)
