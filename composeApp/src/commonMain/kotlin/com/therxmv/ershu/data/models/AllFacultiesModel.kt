package com.therxmv.ershu.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class AllFacultiesModel(
    @SerialName("all_faculties") val allFaculties: List<FacultyModel> = emptyList(),
)

@Serializable
data class FacultyModel(
    @SerialName("faculty_name") val facultyName: String,
    @SerialName("folder_name") val folderName: String,
)