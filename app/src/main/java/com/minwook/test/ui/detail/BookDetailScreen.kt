package com.minwook.test.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.minwook.test.ui.theme.Gray90
import com.minwook.test.ui.theme.White
import kotlin.math.sign

@Composable
fun BookDetailScreen(
    navController: NavController,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    val loadingState = viewModel.dataLoadingState.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ) {

        state.value.bookInfo?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.value.bookInfo?.image)
                        .build(),
                    contentDescription = (state.value.bookInfo?.title) ?: "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 12f)
                )

                // title
                BookDeatilTitleRow(
                    text = "${state.value.bookInfo?.title}",
                    textSize = 24,
                    topPadding = 8
                )

                //subtitle
                if (state.value.bookInfo?.subtitle?.isNotEmpty() == true) {
                    BookDeatilTitleRow(
                        text = "${state.value.bookInfo?.subtitle}",
                        textSize = 18,
                        textColor = Color.DarkGray,
                        topPadding = 4
                    )
                }

                // authors / publisher / language
                BookDeatilRow(
                    title = "Authors / Publisher / Language",
                    content ="${(state.value.bookInfo?.authors)} / ${(state.value.bookInfo?.publisher)} / ${(state.value.bookInfo?.language)}",
                    modifier = Modifier.padding(top = 16.dp)
                )

                //ISBN10 / ISBN13
                BookDeatilRow(
                    title = "ISBN10 / ISBN13",
                    content ="${(state.value.bookInfo?.isbn10)} / ${(state.value.bookInfo?.isbn13)}",
                )

                // page / year
                BookDeatilRow(
                    title = "Page / Year",
                    content = "${(state.value.bookInfo?.pages)} page / ${(state.value.bookInfo?.year)}",
                )

                // rating
                BookDeatilRow(
                    title = "Rating",
                    content = "${(state.value.bookInfo?.rating)}",
                )

                // desc
                BookDeatilRow(
                    title = "Desc",
                    content = "${(state.value.bookInfo?.desc)}",
                )

                // price
                BookDeatilRow(
                    title = "Price",
                    content = "${(state.value.bookInfo?.price)}",
                )

                // URL
                BookDeatilRow(
                    title = "URL",
                    content = "${(state.value.bookInfo?.url)}",
                )

                //PDF
                if (state.value.bookInfo?.pdf?.isNotEmpty() == true) {
                    state.value.bookInfo?.let {
                        BookDeatilPdfInfoRow(it.pdf)
                    }
                }
            }
        }

        // 로딩 indicator
        if (loadingState.value) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp).align(Alignment.Center),
                color = Color.Black,
            )
        }
    }
}

/*
* 도서 제목
* */
@Composable
fun BookDeatilTitleRow(text: String, textSize: Int, textColor: Color = Color.Black,topPadding: Int) {
    Text(
        modifier = Modifier.padding(top = topPadding.dp),
        text = text,
        style = TextStyle(
            color = textColor,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

/*
* 도서 정보
* */
@Composable
fun BookDeatilRow(title: String, content: String, modifier: Modifier = Modifier.padding(top = 10.dp)) {
    Text(
        modifier = modifier,
        text = title,
        style = TextStyle(
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    )

    Text(
        text = content,
        style = TextStyle(
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    )
}

/*
* 도서 PDF
* */
@Composable
fun BookDeatilPdfInfoRow(pdf: Map<String,String>) {
    Text(
        modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
        text = "PDF",
        style = TextStyle(
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    )

    pdf.forEach { _pdf ->
        Text(
            text = "${_pdf.key} : ${_pdf.value}",
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        )
    }
}