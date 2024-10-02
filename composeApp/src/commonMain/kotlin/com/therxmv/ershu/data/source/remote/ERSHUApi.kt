package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.ExamCalendarModel
import com.therxmv.ershu.data.models.RatingModel
import com.therxmv.ershu.data.models.ScheduleModel

interface ERSHUApi {
    suspend fun getAllFaculties(): Result<AllFacultiesModel>
    suspend fun getAllSpecialties(facultyPath: String): Result<AllSpecialtiesModel>
    suspend fun getSchedule(facultyPath: String, year: String, specialty: String): Result<ScheduleModel>
    suspend fun getCallSchedule(): Result<AllCallsScheduleModel>
    suspend fun getRatingBySpecialty(faculty: String, year: String, specialty: String): Result<RatingModel>
    suspend fun getExamCalendar(faculty: String, year: String, specialty: String): Result<ExamCalendarModel>
}