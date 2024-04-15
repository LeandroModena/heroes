package dev.modena.marvel.model

data class Data(
    val offset: Long,
    val limit: Long,
    val total: Long,
    val count: Long,
    val results: List<HeroData>,
)