package com.therxmv.ershu.data.source.local.database

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.db.Profile

interface ERSHUDatabaseApi {
    fun getProfileInfo(): Profile?
    fun setProfileInfo(year: String?, specialty: String?)
    fun clearUserInfo()

    fun getAllSpecialties(): AllSpecialtiesModel?
    fun setAllSpecialties(allSpecialtiesModel: AllSpecialtiesModel)
    fun clearSpecialties()

    fun getSchedule(specialty: String): ScheduleModel?
    fun setSchedule(scheduleModel: ScheduleModel, specialty: String)
    fun clearSchedule(specialty: String)

    fun getAllCalls(): AllCallsScheduleModel?
    fun setAllCalls(allCallsScheduleModel: AllCallsScheduleModel)
    fun clearCalls()
}