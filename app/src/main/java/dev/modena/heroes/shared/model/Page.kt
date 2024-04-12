package dev.modena.heroes.shared.model

import dev.modena.marvel.model.ResponseMarvel

data class Page(
    val offset: Long,
    val limit: Long,
    val total: Long,
    val count: Long,
) {

    companion object {

        private const val AMOUNT_HERO = 10
        fun createByMarvel(marvel: ResponseMarvel): Page {
            return Page(
                offset = marvel.data.offset,
                limit = marvel.data.limit,
                total = marvel.data.total,
                count = marvel.data.count
            )
        }
    }

    fun getCurrentPage(): Long {
        return offset + count
    }

    fun nextPage(): Long {
        return offset + AMOUNT_HERO
    }

    fun backPage(): Long {
        return offset - AMOUNT_HERO
    }


}