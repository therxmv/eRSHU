package com.therxmv.ershu.data.source.local.database

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.AllFacultiesModel
import com.therxmv.ershu.data.models.AllSpecialtiesModel
import com.therxmv.ershu.data.models.CallScheduleModel
import com.therxmv.ershu.data.models.ScheduleModel
import com.therxmv.ershu.data.source.local.DatabaseDriverFactory
import com.therxmv.ershu.data.source.local.mapper.toDomain
import com.therxmv.ershu.db.ERSHUDatabase
import com.therxmv.ershu.db.Lesson

class ERSHUDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
) : ERSHUDatabaseApi {

    companion object {
        private const val DEFAULT_NAME = "user_name"

        private const val FIRST_SHIFT = "first"
        private const val SECOND_SHIFT = "second"
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

    override fun getAllSpecialties(faculty: String?) = AllSpecialtiesModel(
        database.specialtyQueries
            .getAllSpecialties(faculty)
            .executeAsList()
            .groupBy { it.year }.values.toList()
            .map { it.map { item -> item.toDomain() } }
    )

    override fun setAllSpecialties(allSpecialtiesModel: AllSpecialtiesModel, faculty: String?) {
        clearSpecialties(faculty)
        allSpecialtiesModel.allYears.forEachIndexed { index, list ->
            if (list.isEmpty()) {
                database.specialtyQueries.setSpecialty("${index + 1}", "", faculty)
            } else {
                list.forEach { item ->
                    database.specialtyQueries.setSpecialty("${index + 1}", item.specialtyName, faculty)
                }
            }
        }
    }

    override fun clearSpecialties(faculty: String?) {
        database.specialtyQueries.clearSpecialties(faculty)
    }

    override fun getSchedule(specialty: String) = ScheduleModel(
        database.lessonQueries
            .getAllSchedule(specialty)
            .executeAsList()
            .groupBy { it.dayOfWeek }.values.toList()
            .map { it.validate().map { item -> item.toDomain() } }
    )

    override fun setSchedule(scheduleModel: ScheduleModel, specialty: String) {
        clearSchedule(specialty)
        scheduleModel.week.forEachIndexed { index, list ->
            if (list.isEmpty()) {
                database.lessonQueries.setSchedule(null, null, null, "${index + 1}", specialty)
            } else {
                list.forEach { item ->
                    database.lessonQueries.setSchedule(item.lessonName, item.lessonNumber, item.link, "${index + 1}", specialty)
                }
            }
        }
    }

    override fun clearSchedule(specialty: String) {
        database.lessonQueries.clearSchedule(specialty)
    }

    override fun getAllCalls() = AllCallsScheduleModel(
        first = CallScheduleModel(
            database.callsQueries.getAllCalls(FIRST_SHIFT).executeAsList().map { it.time.orEmpty() }
        ),
        second = CallScheduleModel(
            database.callsQueries.getAllCalls(SECOND_SHIFT).executeAsList().map { it.time.orEmpty() }
        ),
    )

    override fun setAllCalls(allCallsScheduleModel: AllCallsScheduleModel) {
        clearCalls()

        allCallsScheduleModel.first.time.forEach {
            database.callsQueries.setCall(FIRST_SHIFT, it)
        }

        allCallsScheduleModel.second.time.forEach {
            database.callsQueries.setCall(SECOND_SHIFT, it)
        }
    }

    override fun clearCalls() {
        database.callsQueries.clearCalls()
    }

    override fun getAllFaculties() = AllFacultiesModel(
        database.facultyQueries.getAllFaculties().executeAsList().map { it.toDomain() }
    )

    override fun setAllFaculties(allFacultiesModel: AllFacultiesModel) {
        clearFaculties()
        allFacultiesModel.allFaculties.forEach {
            database.facultyQueries.setFaculty(it.facultyName, it.folderName)
        }
    }

    override fun clearFaculties() {
        database.facultyQueries.clearFaculties()
    }

    private fun List<Lesson>.validate() = if (this.size <= 1) {
        this.filter { it.name.isNullOrEmpty().not() }
    } else {
        this
    }
}