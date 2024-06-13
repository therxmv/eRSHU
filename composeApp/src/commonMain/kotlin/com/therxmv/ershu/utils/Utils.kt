package com.therxmv.ershu.utils

import com.therxmv.ershu.Res

fun isValidLink(link: String) = link.matches("[A-Za-z].*://\\S*".toRegex())

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toDayOfWeek() = when(this) {
    0 -> Res.string.schedule_monday
    1 -> Res.string.schedule_tuesday
    2 -> Res.string.schedule_wednesday
    3 -> Res.string.schedule_thursday
    4 -> Res.string.schedule_friday
    5 -> Res.string.schedule_saturday
    6 -> Res.string.schedule_sunday
    else -> Res.string.schedule_unknown_day
}