package com.minwook.data.repository

import com.minwook.data.remote.BookApi
import com.minwook.domain.model.BookInfo
import com.minwook.domain.model.ResultData
import com.minwook.domain.model.SearchBook
import com.minwook.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: BookApi
) : BookRepository {

    override suspend fun getSearchBook(
        query: String,
        page: Int
    ): Flow<ResultData<List<SearchBook>>> = flow {
        api.getSearchBook(query = query, page = page)
            .onSuccess { _searchData ->
                emit(
                    ResultData.Success(data = _searchData.books)
                )
            }
            .onFailure { _error ->
                emit(
                    ResultData.Error(message = _error.localizedMessage ?: "")
                )
            }
    }

    override suspend fun getBookInfo(isbn13: String): Flow<ResultData<BookInfo>> = flow {
        api.getBookInfo(isbn = isbn13)
            .onSuccess { _bookInfo ->
                emit(
                    ResultData.Success(data = _bookInfo)
                )
            }
            .onFailure { _error ->
                emit(
                    ResultData.Error(message = _error.localizedMessage ?: "")
                )
            }
    }
}