package com.therxmv.ershu.ui.schedule.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.Res
import com.therxmv.ershu.analytics.AnalyticsApi
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.RemindersApi
import com.therxmv.ershu.data.reminders.event.EventProviderApi
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.local.reminders.RemindersLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.Result
import com.therxmv.ershu.ui.base.AppbarTitleStore
import com.therxmv.ershu.ui.base.BaseViewModel
import com.therxmv.ershu.ui.base.ViewModelDisposer
import com.therxmv.ershu.ui.schedule.viewmodel.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.viewmodel.utils.ScheduleUiState
import com.therxmv.ershu.ui.schedule.viewmodel.utils.ScheduleUiState.Loading
import com.therxmv.ershu.ui.schedule.viewmodel.utils.ScheduleUiState.Success
import com.therxmv.ershu.utils.Analytics.DELETE_REMINDER_CLICK
import com.therxmv.ershu.utils.Analytics.SET_REMINDER_CLICK
import com.therxmv.ershu.utils.toInt
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val ershuApi: ERSHUApi,
    private val remindersApi: RemindersApi,
    private val eventProviderApi: EventProviderApi,
    private val remindersLocalSourceApi: RemindersLocalSourceApi,
    private val profileLocalSourceApi: ProfileLocalSourceApi,
    private val analyticsApi: AnalyticsApi,
    private val appbarTitleStore: AppbarTitleStore,
) : ScreenModel, ViewModelDisposer {

    private val _uiState = MutableStateFlow<ScheduleUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    private val _expandedList = MutableStateFlow(emptyList<Boolean>())
    val expandedList = _expandedList.asStateFlow()

    private val _remindersState = MutableStateFlow<List<ReminderModel>>(emptyList())
    val remindersState = _remindersState.asStateFlow()

    private val _permissionDialogState = MutableStateFlow(false)
    val permissionDialogState = _permissionDialogState.asStateFlow()

    override fun resetState() {
        _uiState.update { Loading }
    }

    fun loadData() {
        val profile = profileLocalSourceApi.getProfileInfo()

        appbarTitleStore.titleFlow.update {
            "${Res.string.schedule_title} ${profile?.specialtyName.orEmpty()}"
        }

        screenModelScope.launch {
            delay(150)
            val result = ershuApi.getSchedule(
                profile?.facultyPath.orEmpty(),
                profile?.year.orEmpty(),
                profile?.specialtyName.orEmpty(),
            )
            val schedule = result.value

            analyticsApi.scheduleOpened(profile?.facultyName.orEmpty(), profile?.specialtyName.orEmpty())
            _uiState.update { Success(schedule) }

            val failure = result as? Result.Failure
            BaseViewModel.setChildIsOffline(
                isOffline = failure != null,
                isBadRequest = failure?.isBadRequest ?: false,
            )

            if (_expandedList.value.isEmpty()) {
                _expandedList.update {
                    delay(300)
                    schedule.week.getExpandedList()
                }
            }

            updateRemindersState()
        }
    }

    fun onEvent(event: ScheduleUiEvent) {
        when (event) {
            is ScheduleUiEvent.ExpandDay -> {
                val newList = _expandedList.value.toMutableList()
                val newValue = newList.getOrElse(event.index) { false }.not()

                if (event.index in 0 until newList.size) {
                    newList[event.index] = newValue
                } else {
                    newList.add(event.index, newValue)
                }

                _expandedList.update { newList }
            }

            is ScheduleUiEvent.SetNotification -> {
                remindersApi.isPermissionGranted {
                    if (it) {
                        val profile = profileLocalSourceApi.getProfileInfo()
                        analyticsApi.onClickEvent(SET_REMINDER_CLICK)
                        setNotification(event.item, profile?.facultyPath.orEmpty())
                    } else {
                        _permissionDialogState.update { true }
                    }
                }
            }

            is ScheduleUiEvent.DeleteNotification -> {
                remindersApi.deleteNotification(event.reminder)
                remindersLocalSourceApi.deleteReminder(event.reminder.reminderId)
                analyticsApi.onClickEvent(DELETE_REMINDER_CLICK)
                updateRemindersState()
            }

            is ScheduleUiEvent.PermissionDialogAction -> {
                if (event.isDeny) {
                    _permissionDialogState.update { false }
                } else {
                    remindersApi.requestNotificationPermission()
                }
            }
        }
    }

    fun sendAnalytics(eventName: String) {
        analyticsApi.onClickEvent(eventName)
    }

    private fun setNotification(item: LessonModel, faculty: String) {
        val event = eventProviderApi.getEvent(item, faculty)

        if (remindersLocalSourceApi.isReminderExists(event).not()) {
            remindersApi.addNotification(event) {
                remindersLocalSourceApi.setReminder(event)
                updateRemindersState()
            }
        }
    }

    private fun updateRemindersState() {
        _remindersState.update {
            remindersLocalSourceApi.getAllReminders().toMutableList()
        }
    }

    private fun List<List<LessonModel>>.getExpandedList(): List<Boolean> {
        val date = GMTDate()
        val isNextDay = date.hours >= 14
        val currentDay = (date.dayOfWeek.ordinal + isNextDay.toInt())
            .takeIf { it < this.size } ?: 0

        return List(this.size) { index ->
            currentDay == index && this[index].isNotEmpty()
        }
    }
}