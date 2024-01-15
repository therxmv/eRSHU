package com.therxmv.ershu.data.source.local.profile

import com.therxmv.ershu.data.source.local.database.ERSHUDatabase
import com.therxmv.ershu.data.source.local.database.ERSHUDatabaseApi

class ProfileLocalSource(
    private val ershuDatabaseApi: ERSHUDatabaseApi = ERSHUDatabase(),
) : ProfileLocalSourceApi {

    override fun getProfileInfo() = ershuDatabaseApi.getProfileInfo()

    override fun setProfileInfo(year: String?, specialty: String?) {
        ershuDatabaseApi.setProfileInfo(year, specialty)
    }
}