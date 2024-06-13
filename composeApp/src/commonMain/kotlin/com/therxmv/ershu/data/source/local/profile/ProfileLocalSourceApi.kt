package com.therxmv.ershu.data.source.local.profile

import com.therxmv.ershu.db.Profile

interface ProfileLocalSourceApi {
    fun getProfileInfo(): Profile?
    fun setProfileInfo(year: String?, facultyPath: String?, facultyName: String?, specialtyName: String?)
    fun clearUserInfo()
}