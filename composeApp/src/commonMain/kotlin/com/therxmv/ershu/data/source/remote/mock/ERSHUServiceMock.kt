package com.therxmv.ershu.data.source.remote.mock

import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.utils.MockData

class ERSHUServiceMock : ERSHUApi {

    override suspend fun getAllSpecialties() = MockData.allSpecialtyModel

    override suspend fun getSchedule(year: String, specialty: String) = MockData.scheduleModel

    override suspend fun getCallSchedule() = MockData.allCallsScheduleModel
}