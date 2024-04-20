package com.getir.finalcase.common.domain

sealed class ViewState<out T> {

    object Loading: ViewState<Nothing>()
    data class Error(val error: String): ViewState<Nothing>()
    data class Success<out T>(val result: T): ViewState<T>()

}