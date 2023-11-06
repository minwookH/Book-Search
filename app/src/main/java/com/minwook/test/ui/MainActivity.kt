package com.minwook.test.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.minwook.test.ui.main.SearchBookScreen
import com.minwook.test.ui.detail.BookDetailScreen
import com.minwook.test.ui.theme.BookProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookProjectTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.SearchBook.name
                ) {
                    composable(
                        route = Screen.SearchBook.name
                    ) { _backstackEntry ->
                        SearchBookScreen(
                            onBookDetailMove = {
                                navController.navigate(
                                    "${Screen.BookDetail.name}/${it}"
                                )
                            }
                        )
                    }

                    composable(
                        route = "${Screen.BookDetail.name}/{isbn13}",
                        arguments = arrayListOf(
                            navArgument("isbn13") {
                                type = NavType.StringType
                            }
                        )
                    ) { _backstackEntry ->
                        BookDetailScreen(navController = navController)
                    }
                }
            }
        }
    }
}