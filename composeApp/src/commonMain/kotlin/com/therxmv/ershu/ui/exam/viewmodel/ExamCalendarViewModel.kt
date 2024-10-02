package com.therxmv.ershu.ui.exam.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.ExamCalendarModel
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.ui.base.AppbarTitleStore
import com.therxmv.ershu.ui.exam.view.ExamCalendarData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExamCalendarViewModel(
    private val ershuApi: ERSHUApi,
    private val profileLocalSourceApi: ProfileLocalSourceApi,
    appbarTitleStore: AppbarTitleStore,
) : ScreenModel {

    private val _dataStateFlow = MutableStateFlow<DataState>(DataState.Idle)
    val dataStateFlow = _dataStateFlow.asStateFlow()

    init {
        appbarTitleStore.titleFlow.update { Res.string.exams_title }
    }

    fun loadData() {
        screenModelScope.launch {
            _dataStateFlow.update { DataState.Loading }

            val profile = profileLocalSourceApi.getProfileInfo() ?: return@launch

            val data = ershuApi.getExamCalendar(
                profile.facultyPath.orEmpty(),
                profile.year.orEmpty(),
                profile.specialtyName.orEmpty(),
            ).value.toUiData()

            _dataStateFlow.update { DataState.Ready(data) }
        }
    }

    private fun ExamCalendarModel.toUiData(): ExamCalendarData {
        val emptyPlaceholder = ExamCalendarData.Item.EmptyPlaceholder(Res.string.exams_no_data)
        val exams = exams.map {
            ExamCalendarData.Item.Exam(
                teacher = it.teacher.orEmpty(),
                lesson = it.lesson,
                date = it.date.orEmpty(),
            )
        }.ifEmpty { listOf(emptyPlaceholder) }

        val zalik = zalik.map {
            ExamCalendarData.Item.Zalik(
                lesson = it.lesson,
            )
        }.ifEmpty { listOf(emptyPlaceholder) }

        return ExamCalendarData(
            items = buildList {
                ExamCalendarData.Item.Title(Res.string.exams_list_title).also { add(it) }
                addAll(exams)

                ExamCalendarData.Item.Title(Res.string.zalik_list_title).also { add(it) }
                addAll(zalik)
            }
        )
    }

    sealed interface DataState {
        data object Idle : DataState
        data object Loading : DataState
        data class Ready(val data: ExamCalendarData) : DataState
    }
}