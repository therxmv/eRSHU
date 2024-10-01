package com.therxmv.ershu.analytics

// TODO empty for now
actual class AnalyticsApi {
    actual fun onClickEvent(eventName: String) {}
    actual fun specialtyInfoSaved(faculty: String, specialty: String) {}
    actual fun scheduleOpened(faculty: String, specialty: String) {}
    actual fun homeItemClicked(screenName: String) {}
}