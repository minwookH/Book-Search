package com.minwook.test.ui.main

import com.minwook.test.ui.base.UiEffect
import com.minwook.test.ui.base.UiEvent
import com.minwook.test.ui.base.UiState
import com.minwook.domain.model.SearchBook

class SearchBookContract {

    sealed class Event : UiEvent {
        data class OnSearchLoad(val query: String) : Event()
        data class OnMoreLoad(val page: Int) : Event()
        data class SetSearchQuery(val query: String) : Event()
        object OnClearSearchQuery : Event()
    }

    data class State(
        val list: List<SearchBook>,
        val searchQuery: String,
        val listPage: Int,
        val isMoreLoad: Boolean
    ) : UiState

    sealed class Effect : UiEffect {
        data class ShowToast(val text : String) : Effect()
    }
}