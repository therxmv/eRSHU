package com.therxmv.ershu.data.source.remote

sealed class Result<T>(open val value: T) {
    data class Success<T>(override val value: T): Result<T>(value)
    data class Failure<T>(override val value: T): Result<T>(value)
}

fun Result<*>.isFailure() = this is Result.Failure<*>