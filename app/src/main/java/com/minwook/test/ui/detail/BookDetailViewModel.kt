package com.minwook.test.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.minwook.test.ui.base.BaseViewModel
import com.minwook.domain.model.ResultData
import com.minwook.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject internal constructor(
    private val bookRepository: BookRepository,
    private val stateHandle: SavedStateHandle
) : BaseViewModel<BookDetailContract.Event, BookDetailContract.State, BookDetailContract.Effect>() {

    init {
        stateHandle.get<String>("isbn13")?.let {
            getBookDetailInfo(it)
        }
    }

    override fun onInitState(): BookDetailContract.State {
        return BookDetailContract.State(
            bookInfo = null
        )
    }

    override fun handleEvent(event: BookDetailContract.Event) {
        when (event) {
            is BookDetailContract.Event.OnLoadData -> {
                getBookDetailInfo(isbn13 = event.isbn13)
            }
        }
    }

    /**
     * ISBN13의 도서 데이터 요청
     *
     * @param isbn13 도서의 ISBN13
     * */
    private fun getBookDetailInfo(isbn13: String) {
        viewModelScope.launch {
            bookRepository.getBookInfo(isbn13)
                .loading()
                .collectLatest { _resultData ->
                    when (_resultData) {
                        is ResultData.Success -> {
                            _resultData.data?.let { _data ->
                                setState {
                                    copy(
                                        bookInfo = _data
                                    )
                                }
                            }
                        }

                        is ResultData.Error -> {
                            setEffect { BookDetailContract.Effect.ShowToast(text = _resultData.message) }
                        }
                    }
                }
        }
    }
}
