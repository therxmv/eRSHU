package com.therxmv.ershu.ui.base

import com.therxmv.ershu.Res
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Storage that holds a mutable [Flow] of appbar title.
 * Those titles are emitted by each screen
 * and intended to be displayed on the Appbar.
 */
class AppbarTitleStore {
    val titleFlow = MutableStateFlow(Res.string.app_name)
}