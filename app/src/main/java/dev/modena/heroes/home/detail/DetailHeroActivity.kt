package dev.modena.heroes.home.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import dagger.hilt.android.AndroidEntryPoint
import dev.modena.heroes.R
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.shared.arch.BaseActivity
import dev.modena.heroes.shared.util.ShareImage
import dev.modena.heroes.shared.util.parcelable
import dev.modena.heroes.shared.util.showDetailHero
import dev.modena.heroes.ui.theme.HeroesTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailHeroActivity : BaseActivity() {

    private lateinit var hero: Hero
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailHeroScreen(
                        hero = hero,
                        onFavoriteClick = { }
                    )
                }
            }
        }
    }

    override fun init() {
        hero = intent.parcelable<Hero>(Hero::class.java.simpleName)!!
    }
}

@Composable
fun DetailHeroScreen(
    hero: Hero,
    onFavoriteClick: (isFavorite: Boolean) -> Unit
) {
    var isFavorite by rememberSaveable { mutableStateOf(hero.isFavorite) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(hero.thumbnailURL)
                        .error(R.drawable.ic_error)
                        .build(),
                    contentDescription = hero.name,
                    modifier = Modifier
                        .height(384.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Inside
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hero.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = hero.description.ifEmpty { "Sem descrição." },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            ShareImage.async(context, hero, "Compartilhar por") {
                                Toast.makeText(context, "Não foi possivel compartilhar a imagem", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                ) {
                    Text(text = "Compartilhar")
                }
            }
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onFavoriteClick.invoke(hero.isFavorite)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite || hero.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite || hero.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

