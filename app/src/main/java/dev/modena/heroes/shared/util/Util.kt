package dev.modena.heroes.shared.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.home.detail.DetailHeroActivity

fun showDetailHero(context: Context, hero: Hero) {
    context.startActivity(
        Intent(context, DetailHeroActivity::class.java).apply {
            putExtra(Hero::class.java.simpleName, hero)
        }
    )
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}