package com.therxmv.ershu.data.models

import com.therxmv.ershu.db.Profile
import com.therxmv.ershu.utils.JavaSerializable

data class ProfileUiData(
    val name: String?,
    val year: String?,
    val facultyPath: String?,
    val facultyName: String?,
    val specialtyName: String?,
) : JavaSerializable

fun Profile.toUiData() = ProfileUiData(
    name = name,
    year = year,
    facultyPath = facultyPath,
    facultyName = facultyName,
    specialtyName = specialtyName,
)

fun ProfileUiData.toProfile() = Profile(
    name = name,
    year = year,
    facultyPath = facultyPath,
    facultyName = facultyName,
    specialtyName = specialtyName,
)