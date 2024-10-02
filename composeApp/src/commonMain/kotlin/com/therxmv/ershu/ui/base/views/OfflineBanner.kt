package com.therxmv.ershu.ui.base.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.utils.Links.RESERVE_LINK

@Composable
fun OfflineBanner(isBadRequest: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val title = if (isBadRequest) Res.string.offline_schedule_update else Res.string.offline_title
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        if (isBadRequest.not()) {
            Hyperlink(
                text = Res.string.offline_open_schedule,
                link = RESERVE_LINK,
            )
        }
    }
}

@Composable
private fun Hyperlink(
    text: String,
    link: String,
) {
    val uriHandler = LocalUriHandler.current

    Text(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                uriHandler.openUri(link)
            },
        text = text,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline,
    )
}