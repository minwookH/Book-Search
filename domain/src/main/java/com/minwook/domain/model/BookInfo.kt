package com.minwook.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookInfo(
    @SerialName(value = "error")
    val error: String = "",
    @SerialName(value = "title")
    val title: String = "",
    @SerialName(value = "subtitle")
    val subtitle: String = "",
    @SerialName(value = "authors")
    val authors: String = "",
    @SerialName(value = "publisher")
    val publisher: String = "",
    @SerialName(value = "language")
    val language: String = "",
    @SerialName(value = "isbn10")
    val isbn10: String = "",
    @SerialName(value = "isbn13")
    val isbn13: String = "",
    @SerialName(value = "pages")
    val pages: String = "",
    @SerialName(value = "year")
    val year: String = "",
    @SerialName(value = "rating")
    val rating: String = "",
    @SerialName(value = "desc")
    val desc: String = "",
    @SerialName(value = "price")
    val price: String = "",
    @SerialName(value = "image")
    val image: String = "",
    @SerialName(value = "url")
    val url: String = "",
    @SerialName(value = "pdf")
    val pdf: Map<String, String> = mapOf()
)