package dev.modena.heroes.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.modena.heroes.R
import dev.modena.heroes.home.favorite.FavoriteHeroActivity
import dev.modena.heroes.home.search.SearchHeroActivity
import dev.modena.heroes.ui.theme.HeroesTheme

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val _viewModel: HomeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = { TopAppBar({ Text(text = getString(R.string.home)) }) }
                    ) {
                        Column(modifier = Modifier.padding(it)) {
                            MainScreen(
                                onClickSearch = { showSearchHero() },
                                onClickFavorite = { showFavoriteHero() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showFavoriteHero() {
        startActivity(Intent(this, FavoriteHeroActivity::class.java))
    }

    private fun showSearchHero() {
        startActivity(Intent(this, SearchHeroActivity::class.java))
    }

}

@Composable
fun MainScreen(
    onClickSearch: () -> Unit,
    onClickFavorite: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickSearch.invoke() }
        ) {
            Text(text = stringResource(R.string.fetch_marvel_hero))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onClickFavorite.invoke()
            }
        ) {
            Text(text = stringResource(R.string.my_favorite_hero))
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen({}, {})
}
