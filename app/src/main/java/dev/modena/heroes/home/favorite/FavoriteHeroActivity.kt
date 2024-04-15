package dev.modena.heroes.home.favorite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import dev.modena.heroes.R
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.shared.arch.BaseActivity
import dev.modena.heroes.shared.model.Page
import dev.modena.heroes.ui.theme.HeroesResultScreen
import dev.modena.heroes.ui.theme.ScreenWithTopBar
import dev.modena.heroes.ui.theme.HeroesTheme

@AndroidEntryPoint
class FavoriteHeroActivity : BaseActivity() {

    private val _viewModel: FavoriteHeroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroesTheme {
                val heroes by _viewModel.heroes.collectAsState(listOf())
                val page by _viewModel.page.collectAsState(null)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWithTopBar(
                        title = getString(R.string.favorites),
                        action = { onBackPressedDispatcher.onBackPressed() }) {
                        FavoriteHeroScreen(
                            heroes = heroes,
                            page = page,
                            onClickFavoriteHero = { _, hero ->
                                _viewModel.deleteFavoriteHero(hero)
                            },
                            onClickPage = {
                                _viewModel.getHeroesByPage(it)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun init() {
        _viewModel.initialize()
    }
}

@Composable
fun FavoriteHeroScreen(
    heroes: List<Hero>,
    page: Page?,
    onClickFavoriteHero: (isFavorite: Boolean, hero: Hero) -> Unit,
    onClickPage: (offset: Long) -> Unit
) {
    HeroesResultScreen(
        heroes = heroes,
        page = page,
        noResultMessage = stringResource(R.string.not_hero_add),
        onClickFavoriteHero = onClickFavoriteHero,
        onClickPage = onClickPage
    )
}

