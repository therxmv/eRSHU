package com.therxmv.ershu.ui.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.ui.home.HomeItemModel
import com.therxmv.ershu.ui.home.utils.HomeItems

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    items: List<HomeItemModel>,
    onItemClick: (HomeItems) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 12.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.title }
        ) { _, item ->
            HomeItem(
                item = item,
                onItemClick = { id ->
                    onItemClick(id)
                }
            )
        }
    }
}