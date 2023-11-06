package com.minwook.test.ui

sealed class Screen(val name: String) {
    object SearchBook : Screen("search_book")
    object BookDetail : Screen("book_detail")
}
