package com.therxmv.ershu.ui.schedule

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val ershuApi: ERSHUApi,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()

    fun getDayOfWeek(index: Int) = when (index) {
        1 -> Res.string.schedule_monday
        2 -> Res.string.schedule_tuesday
        3 -> Res.string.schedule_wednesday
        4 -> Res.string.schedule_thursday
        5 -> Res.string.schedule_friday
        6 -> Res.string.schedule_saturday
        7 -> Res.string.schedule_sunday
        else -> Res.string.schedule_unknown_day
    }

    fun loadSchedule(facultyPath: String, year: String, specialty: String) {
        screenModelScope.launch {
            val result = ershuApi.getSchedule(facultyPath, year, specialty)
            val schedule = result.value
            val callsSchedule = ershuApi.getLocalCallSchedule().value

            _uiState.update {
                it.copy(
                    schedule = schedule,
                    callsSchedule = callsSchedule,
                    expandedList = schedule.week.map { false },
                    isOffline = result.isFailure(),
                )
            }
        }
    }

    fun onEvent(event: ScheduleUiEvent) {
        when (event) {
            is ScheduleUiEvent.ExpandDay -> {
                val newList = _uiState.value.expandedList.toMutableList()
                newList[event.index] = !newList[event.index]

                _uiState.update {
                    it.copy(
                        expandedList = newList,
                    )
                }
            }
        }
    }
}