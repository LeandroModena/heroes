package dev.modena.marvel.model

data class Comics (
    val available: Long,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Long,
)