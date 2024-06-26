package dev.modena.heroes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.modena.heroes.R

@Composable
fun ErrorScreen(
    hasInternet: Boolean,
    tryAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(64.dp),
            painter = painterResource(
                if (hasInternet) R.drawable.ic_negative_smile else R.drawable.ic_mobiledata_off
            ),
            contentDescription = stringResource(R.string.image_error),
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
        Text(
            text = stringResource(
                if (hasInternet) R.string.ops else R.string.no_connection_internet
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = { tryAgain.invoke() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}

@Preview
@Composable
fun NoConnectionScreenPreview() {
    ErrorScreen(true) {

    }
}