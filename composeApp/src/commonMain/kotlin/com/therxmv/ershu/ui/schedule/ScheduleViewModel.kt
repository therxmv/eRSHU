package com.therxmv.ershu.ui.schedule

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.RemindersApi
import com.therxmv.ershu.data.reminders.event.EventProviderApi
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.data.source.local.reminders.RemindersLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState.Success
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState.Loading
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
    private val remindersLocalSourceApi: RemindersLocalSourceApi
) : ScreenModel {

    private val _uiState = MutableStateFlow<ScheduleUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    private val _expandedList = MutableStateFlow(emptyList<Boolean>())
    val expandedList = _expandedList.asStateFlow()

    private val _callsScheduleState = MutableStateFlow<AllCallsScheduleModel?>(null)
    val callsScheduleState = _callsScheduleState.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline = _isOffline.asStateFlow()

    private val _remindersState = MutableStateFlow<List<ReminderModel>>(emptyList())
    val remindersState = _remindersState.asStateFlow()

    private val _permissionDialogState = MutableStateFlow(false)
    val permissionDialogState = _permissionDialogState.asStateFlow()

    fun loadData(facultyPath: String, year: String, specialty: String) {
        screenModelScope.launch {
            val result = ershuApi.getSchedule(facultyPath, year, specialty)
            val schedule = result.value

            loadCalls()

            _uiState.update { Success(schedule) }
            _isOffline.update { result.isFailure() }
            _expandedList.update {
                delay(300)
                schedule.week.getExpandedList()
            }

            updateRemindersState()
        }
    }

    private suspend fun loadCalls() {
        val callsSchedule = ershuApi.getLocalCallSchedule().value

        _callsScheduleState.update { callsSchedule }
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
                        setNotification(event.item, event.faculty)
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