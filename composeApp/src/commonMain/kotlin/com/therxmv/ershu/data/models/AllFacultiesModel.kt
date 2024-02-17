package com.therxmv.ershu.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllFacultiesModel(
    @SerialName("all_faculties") val allFaculties: List<FacultyModel> = emptyList(),
)

@Serializable
data class FacultyModel(
    @SerialName("faculty_name") val facultyName: String,
    @SerialName("folder_name") val folderName: String,
)