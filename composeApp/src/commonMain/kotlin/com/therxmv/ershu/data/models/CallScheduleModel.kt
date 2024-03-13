package com.therxmv.ershu.data.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AllCallsScheduleModel(
    val first: CallScheduleModel = CallScheduleModel(),
    val second: CallScheduleModel = CallScheduleModel(),
)

@Immutable
@Serializable
data class CallScheduleModel(
    @SerialName("time") val time: List<String> = emptyList(),
)