package com.therxmv.ershu.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class AllSpecialtiesModel(
    @SerialName("all_years") val allYears: List<List<SpecialtyModel>> = emptyList(),
)

@Serializable
data class SpecialtyModel(
    @SerialName("specialty_name") val specialtyName: String,
)