package com.therxmv.ershu.data.source.remote.mock

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.utils.MockData
import com.therxmv.ershu.data.source.remote.Result

class ERSHUServiceMock : ERSHUApi {

    override suspend fun getAllFaculties(): Result<AllFacultiesModel> = Result.Success(MockData.allFacultiesModel)

    override suspend fun getAllSpecialties(facultyPath: String): Result<AllSpecialtiesModel> = Result.Success(MockData.allSpecialtyModel)

    override suspend fun getSchedule(facultyPath: String, year: String, specialty: String): Result<ScheduleModel> = Result.Success(MockData.scheduleModel)

    override suspend fun getCallSchedule(): Result<AllCallsScheduleModel> = Result.Success(MockData.allCallsScheduleModel)

    override suspend fun getLocalCallSchedule(): Result<AllCallsScheduleModel> = Result.Success(MockData.allCallsScheduleModel)
}