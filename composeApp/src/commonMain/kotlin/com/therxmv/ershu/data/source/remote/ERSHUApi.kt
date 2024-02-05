package com.therxmv.ershu.data.source.remote

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.ScheduleModel

interface ERSHUApi {
    suspend fun getAllSpecialties(): Result<AllSpecialtiesModel>
    suspend fun getSchedule(year: String, specialty: String): Result<ScheduleModel>
    suspend fun getCallSchedule(): Result<AllCallsScheduleModel>
}