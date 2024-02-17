package com.therxmv.ershu.data.source.local.database

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.db.Profile

interface ERSHUDatabaseApi {
    fun getProfileInfo(): Profile?
    fun setProfileInfo(year: String?, faculty: String?, specialty: String?)
    fun clearUserInfo()

    fun getAllSpecialties(faculty: String?): AllSpecialtiesModel?
    fun setAllSpecialties(allSpecialtiesModel: AllSpecialtiesModel, faculty: String?)
    fun clearSpecialties(faculty: String?)

    fun getSchedule(specialty: String): ScheduleModel?
    fun setSchedule(scheduleModel: ScheduleModel, specialty: String)
    fun clearSchedule(specialty: String)

    fun getAllCalls(): AllCallsScheduleModel?
    fun setAllCalls(allCallsScheduleModel: AllCallsScheduleModel)
    fun clearCalls()

    fun getAllFaculties(): AllFacultiesModel?
    fun setAllFaculties(allFacultiesModel: AllFacultiesModel)
    fun clearFaculties()
}