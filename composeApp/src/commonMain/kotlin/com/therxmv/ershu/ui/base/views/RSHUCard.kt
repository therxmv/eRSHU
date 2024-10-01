package com.therxmv.ershu.ui.base.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RSHUCard(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary,
    ),
    isExpanded: Boolean = false,
    content: @Composable (RowScope.() -> Unit),
) {
    val radius = (if (isExpanded) 0 else 8).dp
    val bottomRadius = animateDpAsState(targetValue = radius)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = colors,
        shape = RoundedCornerShape(
            topEnd = 8.dp,
            topStart = 8.dp,
            bottomEnd = bottomRadius.value,
            bottomStart = bottomRadius.value,
        ),
    ) {
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}