package dev.modena.heroes.search

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.modena.heroes.shared.Hero
import dev.modena.heroes.shared.arch.BaseActivity
import dev.modena.heroes.shared.ui.HeroCard
import dev.modena.heroes.shared.ui.LoadingDataScreen
import dev.modena.heroes.shared.ui.NoConnectionScreen
import dev.modena.heroes.ui.theme.HeroesTheme

@AndroidEntryPoint
class SearchHeroActivity : BaseActivity() {

    private val _viewModel: SearchHeroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroesTheme {
                val hasInternet by _viewModel.hasInternet.collectAsState(true)
                val isLoading by _viewModel.isLoading.collectAsState(false)
                val heroes by _viewModel.heroes.collectAsState(listOf())
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen(
                        hasInternet = hasInternet,
                        isLoading = isLoading,
                        heroes = heroes,
                        checkConnection = { _viewModel.checkConnection() },
                        queryHero = { _viewModel }
                    )
                }
            }
        }
    }

    override fun init() {
        _viewModel.initialize()
    }

}


@Composable
fun SearchScreen(
    hasInternet: Boolean,
    isLoading: Boolean,
    heroes: List<Hero>,
    checkConnection: () -> Unit,
    queryHero: (query: String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    if (hasInternet) {
        Column {
            SearchBar(
                query = query,
                onQueryChanged = { query = it },
                onSearch = { query ->
                    Log.d("SearchScreen", "Buscando por: $query")
                }
            )
            HeroesResultScreen(heroes)
            if (isLoading) {
                LoadingDataScreen()
            }
        }
    } else {
        NoConnectionScreen {
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
        placeholder = { Text("Buscar...") },
        singleLine = true,
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Limpar")
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

@Composable
fun HeroesResultScreen(heroes: List<Hero>) {
    LazyColumn {
        items(heroes.size) {
            val hero = heroes[it]
            HeroCard(hero)
        }
    }

}


@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        hasInternet = true, isLoading = true , listOf(), {}, {})
}
