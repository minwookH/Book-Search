package com.minwook.domain.usecase

import com.minwook.domain.model.ResultData
import com.minwook.domain.model.SearchBook
import com.minwook.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(query: String, page: Int): Flow<ResultData<List<SearchBook>>> =
        flow {
            bookRepository.getSearchBook(query = query, page = page)
                .collect { _data ->
                    _data.data?.let { _list ->
                        emit(
                            ResultData.Success(data = _list)
                        )
                    }
                }
        }
}