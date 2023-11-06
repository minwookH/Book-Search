package com.minwook.test.ui.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.minwook.domain.model.SearchBook
import com.minwook.test.ui.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBookScreen(
    onBookDetailMove: (String) -> Unit,
    viewModel: SearchBookViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val state = viewModel.uiState.collectAsState()
    val loadingState = viewModel.dataLoadingState.collectAsState()
    val localFocusManager = LocalFocusManager.current

    SearchBookEffect(viewModel)

    Scaffold(
        modifier = Modifier.background(Color.White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                // 검색 입력창
                SearchBookTextField(
                    localFocusManager = localFocusManager,
                    searchQuery = state.value.searchQuery,
                    onSearch = {
                        viewModel.setEvent(
                            SearchBookContract.Event.OnSearchLoad(
                                query = it,
                            )
                        )
                    },
                    onValueChange = {
                        viewModel.setEvent(
                            SearchBookContract.Event.SetSearchQuery(
                                query = it,
                            )
                        )
                    },
                    onTrailingIconClick = {
                        viewModel.setEvent(
                            SearchBookContract.Event.OnClearSearchQuery
                        )
                    }
                )

                //검색 결과 리스트
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState
                ) {
                    if (state.value.list.isNotEmpty()) {
                        items(state.value.list) { _searchBook ->
                            SearchBookItem(
                                book = _searchBook,
                                onItemClick = { _data ->
                                    onBookDetailMove.invoke(_data.isbn13)
                                }
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(20.dp))
                    }
                }


                listState.OnBottomReached {
                    if (state.value.isMoreLoad) {
                        viewModel.setEvent(SearchBookContract.Event.OnMoreLoad(page = viewModel.currentState.listPage))
                    }
                }
            }

            // 로딩 Indicator
            if (loadingState.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = Color.Black,
                )
            }
        }
    }
}

/*
* 검색어 입력 TextField
* */
@Composable
fun SearchBookTextField(
    localFocusManager: FocusManager,
    searchQuery: String,
    onSearch: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit
) {
    TextField(
        value = searchQuery,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                // 검색어 전달
                onSearch.invoke(searchQuery)

                // 키보드 내리기
                localFocusManager.clearFocus()
            }
        ),
        onValueChange = { _string ->
            // 변경된 검색어 설정
            onValueChange.invoke(_string)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        // trailingIcon 클릭 (해당 코드에서는 검색 문자열 삭제)
                        onTrailingIconClick.invoke()

                        // 키보드 내리기
                        localFocusManager.clearFocus()
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "검색어를 입력하세요.",
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

}

/*
* 도서 검색 페이지 Effect
* */
@Composable
fun SearchBookEffect(viewModel: SearchBookViewModel) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.onEach { _effect ->
            when (_effect) {
                is SearchBookContract.Effect.ShowToast -> {
                    Toast.makeText(context, _effect.text, Toast.LENGTH_SHORT).show()
                }
            }
        }.collect()
    }
}

/*
* 검색 도서 Item
* */
@Composable
fun SearchBookItem(
    book: SearchBook,
    onItemClick: (SearchBook) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
            .background(Color.LightGray)
            .clickable {
                onItemClick(book)
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Book 이미지
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.image)
                    .build(),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(3f / 4f)
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                // Title
                SearchBookContent(title = "Title", text = book.title)

                // Subtitle
                if (book.subtitle.isNotEmpty()) {
                    SearchBookContent(
                        title = "Subtitle",
                        text = book.subtitle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price / ISBN13
                SearchBookContent(title = "Price / ISBN13", text = "${book.price} / ${book.isbn13}")

                // URL
                SearchBookContent(
                    title = "URL",
                    text = book.url,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/*
* 도서 정보
* */
@Composable
fun SearchBookContent(
    title: String,
    text: String,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = title,
        style = TextStyle(
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    )

    Text(
        text = text,
        style = TextStyle(
            color = Color.Black,
            fontSize = 12.sp
        ),
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) loadMore()
            }
    }
}