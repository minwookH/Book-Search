package com.minwook.domain.model

sealed class ResultData<T>(val data: T? = null, val message: String = "") {
    class Success<T>(data: T) : ResultData<T>(data = data)
    class Error<T>(message: String = "", data: T? = null) : ResultData<T>(data = data, message = message)
}