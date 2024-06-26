package dev.modena.heroes.home.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.modena.heroes.R
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.home.detail.DetailHeroActivity
import dev.modena.heroes.home.detail.DetailHeroActivity.Companion.CHANGE_STATUS_FAVORITE
import dev.modena.heroes.shared.arch.BaseActivity
import dev.modena.heroes.shared.model.Page
import dev.modena.heroes.ui.theme.ErrorScreen
import dev.modena.heroes.ui.theme.HeroesResultScreen
import dev.modena.heroes.ui.theme.HeroesTheme
import dev.modena.heroes.ui.theme.LoadingDataScreen
import dev.modena.heroes.ui.theme.ScreenWithTopBar

@AndroidEntryPoint
class SearchHeroActivity : BaseActivity() {

    private val _viewModel: SearchHeroViewModel by viewModels()
    private val onResultAddEvent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CHANGE_STATUS_FAVORITE) {
                _viewModel.updateFavorite()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroesTheme {
                val hasInternet by _viewModel.hasInternet.collectAsState(true)
                val hasErrors by _viewModel.hasErrors.collectAsState(false)
                val isLoading by _viewModel.isLoading.collectAsState(false)
                val heroes by _viewModel.heroes.collectAsState(listOf())
                val page by _viewModel.page.collectAsState(null)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWithTopBar(
                        title = getString(R.string.search),
                        action = { onBackPressedDispatcher.onBackPressed() }) {
                        SearchScreen(
                            hasInternet = hasInternet,
                            hasError = hasErrors,
                            isLoading = isLoading,
                            heroes = heroes,
                            checkConnection = { _viewModel.tryAgain() },
                            page = page,
                            queryHero = { _viewModel.searchByName(it) },
                            onClickFavoriteHero = { isFavorite, hero ->
                                _viewModel.saveOrDeleteFavoriteHero(isFavorite, hero)
                            },
                            onClickPage = { _viewModel.navigationPage(it) },
                            showDetailHero = { showDetailForResult(it) }
                        )
                    }
                }
            }
        }
    }

    override fun init() {
        _viewModel.initialize()
    }

    private fun showDetailForResult(hero: Hero) {
        onResultAddEvent.launch(Intent(this, DetailHeroActivity::class.java).apply {
            putExtra(Hero::class.java.simpleName, hero)
        })
    }

}


@Composable
fun SearchScreen(
    hasInternet: Boolean,
    hasError: Boolean,
    isLoading: Boolean,
    heroes: List<Hero>,
    page: Page?,
    checkConnection: () -> Unit,
    queryHero: (query: String) -> Unit,
    onClickFavoriteHero: (isFavorite: Boolean, hero: Hero) -> Unit,
    onClickPage: (offset: Long) -> Unit,
    showDetailHero: (hero: Hero) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    if (hasInternet && !hasError) {
        Column {
            SearchBar(
                query = query,
                onQueryChanged = { query = it },
                onSearch = { query ->
                    queryHero.invoke(query)
                }
            )
            if (isLoading) {
                LoadingDataScreen()
            } else {
                HeroesResultScreen(
                    heroes = heroes,
                    page = page,
                    noResultMessage = stringResource(R.string.hero_not_found),
                    onClickFavoriteHero = onClickFavoriteHero,
                    onClickPage = onClickPage,
                    showDetailHero = showDetailHero
                )
            }
        }
    } else {
        ErrorScreen(hasInternet) {
            checkConnection.invoke()
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Nome") },
        singleLine = true,
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.clear))
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        hasInternet = true,
        hasError = false,
        isLoading = false,
        listOf(),
        Page(10, 10, 1000, 10),
        {},
        {},
        { _, _ ->
        },
        {},
        {})
}
