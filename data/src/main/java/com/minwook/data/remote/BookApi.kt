package com.minwook.data.remote

import com.minwook.data.remote.response.SearchBookResponse
import com.minwook.domain.model.BookInfo
import com.minwook.domain.model.SearchBook
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {

    @GET("/1.0/search/{query}/{page}")
    suspend fun getSearchBook(@Path("query") query: String, @Path("page") page: Int): Result<SearchBookResponse>

    @GET("/1.0/books/{isbn}")
    suspend fun getBookInfo(@Path("isbn") isbn: String): Result<BookInfo>
}