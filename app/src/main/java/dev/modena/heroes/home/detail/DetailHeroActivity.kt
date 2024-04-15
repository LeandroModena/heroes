package dev.modena.heroes.home.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import dev.modena.heroes.R
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.shared.arch.BaseActivity
import dev.modena.heroes.shared.ui.ScreenWithTopBar
import dev.modena.heroes.shared.util.ShareImage
import dev.modena.heroes.shared.util.parcelable
import dev.modena.heroes.ui.theme.HeroesTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailHeroActivity : BaseActivity() {

    private lateinit var hero: Hero
    private val _viewModel: DetailHeroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeroesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWithTopBar(
                        title = getString(R.string.details),
                        action = { onBackPressedDispatcher.onBackPressed() }) {
                        DetailHeroScreen(
                            hero = hero,
                            onClickShare = { _viewModel.onClickShareHero() }
                        )
                    }
                }
            }
        }
    }

    override fun init() {
        hero = intent.parcelable<Hero>(Hero::class.java.simpleName)!!
        observeOnClickShare()
    }

    private fun observeOnClickShare() {
        _viewModel.share.observe(this) {
            lifecycleScope.launch {
                ShareImage.async(this@DetailHeroActivity, hero, getString(R.string.share)) {
                    Toast.makeText(
                        this@DetailHeroActivity,
                        getString(R.string.unable_to_share_image),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@Composable
fun DetailHeroScreen(
    hero: Hero,
    onClickShare: () -> Unit
) {
    val scrollState = rememberScrollState()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .verticalScroll(scrollState)
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
                    text = hero.description.ifEmpty { stringResource(R.string.without_description) },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onClickShare.invoke() }
                ) {
                    Text(text = stringResource(R.string.share))
                }
            }
        }
    }
}

