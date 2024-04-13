package dev.modena.marvel.model

data class Thumbnail(
    val path: String,
    val extension: String
) {

    fun getUrl() : String {
        return "$path.$extension"
    }

}
