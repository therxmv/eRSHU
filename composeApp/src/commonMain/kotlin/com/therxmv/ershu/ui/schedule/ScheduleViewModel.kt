package com.therxmv.ershu.ui.schedule

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.RemindersApi
import com.therxmv.ershu.data.reminders.event.EventProviderApi
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.local.reminders.RemindersLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import com.therxmv.ershu.db.Profile
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState.Success
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState.Loading
import com.therxmv.ershu.ui.base.BaseViewModel
import com.therxmv.ershu.ui.base.ViewModelDisposer
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

    fun loadData(data: Profile?) {
        val profile = data ?: profileLocalSourceApi.getProfileInfo()

        screenModelScope.launch {
            delay(150)
            val result = ershuApi.getSchedule(
                profile?.facultyPath.orEmpty(),
                profile?.year.orEmpty(),
                profile?.specialtyName.orEmpty(),
            )
            val schedule = result.value

            _uiState.update { Success(schedule) }
            BaseViewModel.setChildIsOffline(result.isFailure())

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
                newList[event.index] = !newList[event.index]

                _expandedList.update { newList }
            }

            is ScheduleUiEvent.SetNotification -> {
                remindersApi.isPermissionGranted {
                    if (it) {
                        val profile = profileLocalSourceApi.getProfileInfo()
                        setNotification(event.item, profile?.facultyPath.orEmpty())
                    } else {
                        _permissionDialogState.update { true }
                    }
                }
            }

            is ScheduleUiEvent.DeleteNotification -> {
                remindersApi.deleteNotification(event.reminder)
                remindersLocalSourceApi.deleteReminder(event.reminder.reminderId)
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