package com.therxmv.ershu.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.therxmv.ershu.AndroidApp
import com.therxmv.ershu.utils.Analytics.FACULTY_PARAM
import com.therxmv.ershu.utils.Analytics.HOME_ITEM_CLICK
import com.therxmv.ershu.utils.Analytics.SAVE_SPECIALTY
import com.therxmv.ershu.utils.Analytics.SCHEDULE_OPENED
import com.therxmv.ershu.utils.Analytics.SELECTED_FACULTY
import com.therxmv.ershu.utils.Analytics.SELECTED_SCREEN
import com.therxmv.ershu.utils.Analytics.SELECTED_SPECIALTY
import com.therxmv.ershu.utils.Analytics.SPECIALTY_PARAM

actual class AnalyticsApi {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(AndroidApp.INSTANCE.applicationContext)

    actual fun onClickEvent(eventName: String) {
        firebaseAnalytics.logEvent(eventName) {}
    }

    actual fun specialtyInfoSaved(faculty: String, specialty: String) {
        firebaseAnalytics.logEvent(SAVE_SPECIALTY) {
            param(FACULTY_PARAM, faculty)
            param(SPECIALTY_PARAM, specialty)
        }
    }

    actual fun scheduleOpened(faculty: String, specialty: String) {
        firebaseAnalytics.logEvent(SCHEDULE_OPENED) {
            param(SELECTED_FACULTY, faculty)
            param(SELECTED_SPECIALTY, specialty)
        }
    }

    actual fun homeItemClicked(screenName: String) {
        firebaseAnalytics.logEvent(HOME_ITEM_CLICK) {
            param(SELECTED_SCREEN, screenName)
        }
    }
}