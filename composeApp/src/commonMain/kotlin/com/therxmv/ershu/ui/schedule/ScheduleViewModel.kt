package com.therxmv.ershu.ui.schedule

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.LessonModel
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
) : ScreenModel {

    private val _uiState = MutableStateFlow<ScheduleUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    private val _expandedList = MutableStateFlow(emptyList<Boolean>())
    val expandedList = _expandedList.asStateFlow()

    private val _callsScheduleState = MutableStateFlow<AllCallsScheduleModel?>(null)
    val callsScheduleState = _callsScheduleState.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline = _isOffline.asStateFlow()

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

    fun loadData(facultyPath: String, year: String, specialty: String) {
        screenModelScope.launch {
            val result = ershuApi.getSchedule(facultyPath, year, specialty)
            val schedule = result.value

            loadCalls()

            _uiState.update { Success(schedule) }
            _isOffline.update { result.isFailure() }
            _expandedList.update {
                delay(350)
                schedule.week.getExpandedList()
            }
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