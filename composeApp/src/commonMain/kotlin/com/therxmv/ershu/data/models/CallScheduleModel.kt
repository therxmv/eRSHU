package com.therxmv.ershu.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AllCallsScheduleModel(
    val first: CallScheduleModel = CallScheduleModel(),
    val second: CallScheduleModel = CallScheduleModel(),
)

@Serializable
data class CallScheduleModel(
    @SerialName("time") val time: List<String> = emptyList(),
)