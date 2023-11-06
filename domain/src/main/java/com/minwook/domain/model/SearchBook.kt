package com.minwook.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchBook(
    @SerialName(value = "title")
    val title: String = "",
    @SerialName(value = "subtitle")
    val subtitle: String = "",
    @SerialName(value = "isbn13")
    val isbn13: String = "",
    @SerialName(value = "price")
    val price: String = "",
    @SerialName(value = "image")
    val image: String = "",
    @SerialName(value = "url")
    val url: String = ""
)