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
        return offset + limit
    }

    fun backPage(): Long {
        return Math.max(0, offset - limit)
    }

    fun isEnableNextPage(): Boolean {
        return offset + limit < total
    }

    fun isEnableBackPage(): Boolean {
        return offset > 0
    }

}