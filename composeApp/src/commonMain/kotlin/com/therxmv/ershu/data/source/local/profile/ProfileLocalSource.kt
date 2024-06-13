package com.therxmv.ershu.data.source.local.profile

import com.therxmv.ershu.data.source.local.DatabaseDriverFactory
import com.therxmv.ershu.db.ERSHUDatabase

class ProfileLocalSource(
    databaseDriverFactory: DatabaseDriverFactory,
) : ProfileLocalSourceApi {

    companion object {
        private const val DEFAULT_NAME = "user_name"
    }

    private val database = ERSHUDatabase(databaseDriverFactory.createDriver())

    override fun getProfileInfo() = database.profileQueries
        .getUserInfo(DEFAULT_NAME)
        .executeAsOneOrNull()

    override fun setProfileInfo(year: String?, faculty: String?, specialty: String?) {
        clearUserInfo()
        database.profileQueries.setUserInfo(
            name = DEFAULT_NAME,
            year = year,
            faculty = faculty,
            specialty = specialty
        )
    }

    override fun clearUserInfo() {
        database.profileQueries.clearUserInfo()
    }
}