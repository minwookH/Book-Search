package com.minwook.data.remote.response

import com.minwook.domain.model.SearchBook
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchBookResponse(
    @SerialName(value = "total")
    val total: String,
    @SerialName(value = "page")
    val page: String,
    @SerialName(value = "books")
    val books: List<SearchBook>,
    @SerialName(value = "error")
    val error: String,
)