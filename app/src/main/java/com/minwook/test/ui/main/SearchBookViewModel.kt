package com.minwook.test.ui.main

import androidx.lifecycle.viewModelScope
import com.minwook.test.ui.base.BaseViewModel
import com.minwook.domain.model.ResultData
import com.minwook.domain.usecase.GetSearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject internal constructor(
    private val getSearchBookUseCase: GetSearchBookUseCase
) : BaseViewModel<SearchBookContract.Event, SearchBookContract.State, SearchBookContract.Effect>() {

    override fun onInitState(): SearchBookContract.State {
        return SearchBookContract.State(
            list = listOf(),
            searchQuery = "",
            listPage = 1,
            isMoreLoad = false,
        )
    }

    override fun handleEvent(event: SearchBookContract.Event) {
        when (event) {
            is SearchBookContract.Event.OnSearchLoad -> { // 검색어 초기 검색
                if (event.query.isBlank()) { // 검색어가 빈칸인경우
                    setEffect { SearchBookContract.Effect.ShowToast(text = "검색어를 입력해주세요.") }
                } else {
                    // 검색 데이터가 있다면 초기화
                    if (currentState.list.isNotEmpty()) {
                        setState {
                            copy(
                                list = listOf()
                            )
                        }
                    }

                    getSearchBookList(query = event.query)
                }
            }

            is SearchBookContract.Event.OnMoreLoad -> { // 페이징 처리
                getSearchBookList(query = currentState.searchQuery, page = event.page + 1)
            }

            is SearchBookContract.Event.SetSearchQuery -> { // 검색어 설정
                setState {
                    copy(
                        searchQuery = event.query
                    )
                }
            }

            is SearchBookContract.Event.OnClearSearchQuery -> { // 검색어 삭제
                setState {
                    copy(
                        searchQuery = ""
                    )
                }
            }
        }
    }

    /**
     * 검색어에 해당하는 데이터 요청
     *
     * @param query 검색어
     * @param page 페이지 번호
    * */
    private fun getSearchBookList(query: String, page: Int = 1) {
        viewModelScope.launch {
            getSearchBookUseCase(query = query, page = page)
                .loading()
                .collectLatest { _resultData ->
                    when (_resultData) {
                        is ResultData.Success -> {
                            _resultData.data?.let { _list ->
                                val tempList = currentState.list.toMutableList()
                                tempList.addAll(_list)

                                setState {
                                    copy(
                                        list = tempList.toList(),
                                        listPage = page,
                                        isMoreLoad = _list.isNotEmpty()
                                    )
                                }
                            }
                        }

                        is ResultData.Error -> {
                            setEffect { SearchBookContract.Effect.ShowToast(text = _resultData.message) }
                        }
                    }
                }
        }
    }
}
