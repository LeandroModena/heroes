package dev.modena.heroes.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.modena.heroes.R
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.shared.model.Page

@Composable
fun HeroesResultScreen(
    heroes: List<Hero>,
    page: Page?,
    noResultMessage: String,
    onClickFavoriteHero: (isFavorite: Boolean, hero: Hero) -> Unit,
    onClickPage: (offset: Long) -> Unit
) {
    page?.let {
        if (heroes.isNotEmpty()) {
            LazyColumn {
                items(heroes.size) {
                    val hero = heroes[it]
                    HeroCard(hero) {
                        onClickFavoriteHero.invoke(hero.isFavorite, hero)
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                onClickPage.invoke(page.backPage())
                            },
                            enabled = page.isEnableBackPage()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "remove"
                            )
                        }
                        Text(text = page.getCurrentPage().toString())
                        Text(text = stringResource(R.string.page_tab))
                        Text(text = page.total.toString())
                        IconButton(
                            onClick = {
                                onClickPage.invoke(page.nextPage())
                            },
                            enabled = page.isEnableNextPage()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_next),
                                contentDescription = "remove"
                            )
                        }
                    }
                }
            }
        } else {
            NoResultScreen(noResultMessage)
        }
    } ?: Text(text = stringResource(R.string.no_data_to_present))


}