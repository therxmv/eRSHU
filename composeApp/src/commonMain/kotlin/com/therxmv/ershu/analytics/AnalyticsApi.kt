package com.therxmv.ershu.analytics

expect class AnalyticsApi() {
    fun onClickEvent(eventName: String)
    fun specialtyInfoSaved(faculty: String, specialty: String)
    fun scheduleOpened(faculty: String, specialty: String)
    fun homeItemClicked(screenName: String)
}