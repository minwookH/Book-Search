package com.minwook.test.ui.detail

import com.minwook.domain.model.BookInfo
import com.minwook.test.ui.base.UiEffect
import com.minwook.test.ui.base.UiEvent
import com.minwook.test.ui.base.UiState

class BookDetailContract {

    sealed class Event : UiEvent {
        data class OnLoadData(val isbn13: String) : Event()
    }

    data class State(
        val bookInfo: BookInfo?
    ) : UiState

    sealed class Effect : UiEffect {
        data class ShowToast(val text : String) : Effect()
    }
}