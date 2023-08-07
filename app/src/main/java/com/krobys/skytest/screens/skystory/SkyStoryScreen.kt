package com.krobys.skytest.screens.skystory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import com.krobys.skytest.screens.skynews.BigNewsPhoto
import com.krobys.skytest.tools.AssetsTool.getJsonFromAssets
import com.krobys.skytest.ui.theme.SkyTestTheme

@Composable
fun SkyStoryScreen(
    viewModel: SkyStoryUIInteractor = hiltViewModel<SkyStoryViewModel>()
) {
    val skyStoryState = viewModel.skyStoryStateFlow.collectAsState()

    skyStoryState.value?.let { skyStory ->
        StoryBlock(skyStory = skyStory)
    }
}


@Composable
private fun TopBlock(skyStoryState: SkyStoryResponse) {
    Box(modifier = Modifier.fillMaxSize()) {
        BigNewsPhoto(
            photoUrl = skyStoryState.heroImage.imageUrl,
            contentDescription = skyStoryState.heroImage.accessibilityText,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Black, Color.Transparent),
                            startY = size.height,
                            endY = size.height * 0.5f
                        )
                    )
                }
        )

        Text(
            text = skyStoryState.headline,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
        )
    }
}



@Composable
private fun StoryBlock(skyStory: SkyStoryResponse) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.statusBarsPadding())
        }
        item {
            TopBlock(skyStoryState = skyStory)
        }
        items(skyStory.contents) {
            when (it.type) {
                "image" -> {
                    it.url?.let { imageUrl ->
                        PhotoContent(
                            imageUrl = imageUrl,
                            contentDescription = it.accessibilityText
                        )
                    }
                }

                "paragraph" -> {
                    it.text?.let { text ->
                        TextContent(text = text)
                    }
                }
                else -> {
                    //unavailable type, do nothing
                }
            }
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun TextContent(text: String) {
    Text(modifier = Modifier.padding(8.dp), text = text)
}

@Composable
private fun PhotoContent(imageUrl: String, contentDescription: String?) {
    BigNewsPhoto(
        modifier = Modifier.padding(vertical = 8.dp),
        photoUrl = imageUrl,
        contentDescription = contentDescription
    )
}

@Preview
@Composable
private fun SkyStoryScreenPreview() {
    SkyTestTheme {
        val testStory = getJsonFromAssets<SkyStoryResponse>(
            LocalContext.current,
            "json/stories/sample_story1.json"
        ).getOrThrow()
        SkyStoryScreen(SkyStoryUIInteractor.getTestSkyStoryUIInteractor(testStory))
    }
}