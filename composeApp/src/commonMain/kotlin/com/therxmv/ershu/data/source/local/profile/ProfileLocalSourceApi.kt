package com.therxmv.ershu.data.source.local.profile

import com.therxmv.ershu.db.Profile

interface ProfileLocalSourceApi {
    fun getProfileInfo(): Profile?
    fun setProfileInfo(year: String?, specialty: String?)
}