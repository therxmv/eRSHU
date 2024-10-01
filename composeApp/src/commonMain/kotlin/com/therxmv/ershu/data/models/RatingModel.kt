package com.therxmv.ershu.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class RatingModel(
    @SerialName("list") val list: List<RatingItem> = emptyList(),
)

@Serializable
data class RatingItem(
    @SerialName("name") val name: String,
    @SerialName("credits") val credits: Int,
)