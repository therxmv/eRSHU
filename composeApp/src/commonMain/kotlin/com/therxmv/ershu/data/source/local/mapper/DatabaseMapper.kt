package com.therxmv.ershu.data.source.local.mapper

import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.db.Faculty
import com.therxmv.ershu.db.Lesson
import com.therxmv.ershu.db.Specialty

fun Specialty.toDomain() = SpecialtyModel(this.name.orEmpty())

fun Lesson.toDomain() = LessonModel(this.name, this.number.orEmpty(), this.link)

fun Faculty.toDomain() = FacultyModel(this.name.orEmpty(), this.folder.orEmpty())