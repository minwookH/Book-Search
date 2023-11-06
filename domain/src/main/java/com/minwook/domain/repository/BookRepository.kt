package com.minwook.domain.repository

import com.minwook.domain.model.BookInfo
import com.minwook.domain.model.ResultData
import com.minwook.domain.model.SearchBook
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun getSearchBook(query: String, page: Int): Flow<ResultData<List<SearchBook>>>

    suspend fun getBookInfo(isbn13: String): Flow<ResultData<BookInfo>>

}