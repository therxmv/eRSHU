package com.therxmv.ershu.ui.rating.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.RatingItem
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.Result
import com.therxmv.ershu.ui.base.AppbarTitleStore
import com.therxmv.ershu.ui.base.ViewModelDisposer
import com.therxmv.ershu.ui.rating.viewmodel.utils.InputState
import com.therxmv.ershu.ui.rating.viewmodel.utils.RatingState
import com.therxmv.ershu.ui.rating.viewmodel.utils.RatingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RatingViewModel(
    private val ershuApi: ERSHUApi,
    private val profileLocalSourceApi: ProfileLocalSourceApi,
    appbarTitleStore: AppbarTitleStore,
) : ScreenModel, ViewModelDisposer {

    private val _uiState = MutableStateFlow<RatingUiState>(RatingUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _inputsState = MutableStateFlow<Map<RatingItem, InputState>>(emptyMap())
    val inputsState = _inputsState.asStateFlow()

    private val _ratingState = MutableStateFlow<RatingState>(RatingState.Initial)
    val ratingState = _ratingState.asStateFlow()

    init {
        appbarTitleStore.titleFlow.update { Res.string.rating_title }
    }

    fun loadData() {
        screenModelScope.launch {
            val profile = profileLocalSourceApi.getProfileInfo()
            val result = ershuApi.getRatingBySpecialty(
                faculty = profile?.facultyPath.orEmpty(),
                year = profile?.year.orEmpty(),
                specialty = profile?.specialtyName.orEmpty(),
            )
            val rating = result.value

            if (result is Result.Failure) {
                _uiState.update { RatingUiState.NotAvailable }
            } else {
                if (_inputsState.value.isEmpty()) {
                    _inputsState.update {
                        rating.list.associateWith { InputState.Value("") }
                    }
                }

                _uiState.update {
                    RatingUiState.Ready(rating)
                }
            }
        }
    }

    fun updateInputByIndex(item: RatingItem, value: String) {
        val list = _inputsState.value.toMutableMap()
        list[item] = InputState.Value(value)

        _inputsState.update { list }
    }

    fun calculateRating() {
        val inputs = _inputsState.value

        if (isValidInputs()) {
            val creditsSum = inputs.keys.sumOf { it.credits }

            var examSum = 0
            inputs.forEach {
                val score = (it.value as InputState.Value).value.toInt()
                examSum += score * it.key.credits
            }

            val result = 90F * (examSum / (creditsSum * 100F))

            _ratingState.update { RatingState.Success(result) }
        } else {
            _ratingState.update { RatingState.Error } // TODO provide different texts like "академ заборгованість"
        }
    }

    private fun isValidInputs(): Boolean {
        val inputs = _inputsState.value

        return inputs.all {
            val score = (it.value as? InputState.Value)?.value?.toIntOrNull()
            score != null && score >= 60 && score <= 100
        }
    }

    override fun resetState() {
        _uiState.update { RatingUiState.Loading }
        _inputsState.update { emptyMap() }
        _ratingState.update { RatingState.Initial }
    }
}