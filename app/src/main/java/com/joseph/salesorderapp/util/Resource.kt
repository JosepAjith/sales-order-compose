package com.joseph.salesorderapp.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>: Resource<T>()
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T?) = Success(data!!)
        fun <T> error(msg: String) = Error<T>(msg)
    }
}
