package com.therxmv.ershu.utils

fun isValidLink(link: String) = link.matches("[A-Za-z].*://\\S*".toRegex())
fun Boolean.toInt() = if (this) 1 else 0