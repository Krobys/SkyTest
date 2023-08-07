package com.krobys.skytest.screens.skynews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.krobys.skytest.R
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse
import com.krobys.skytest.tools.AssetsTool.getJsonFromAssets
import com.krobys.skytest.tools.TimeTool.formatPostDate
import com.krobys.skytest.ui.theme.GreyPlaceholderBackground
import com.krobys.skytest.ui.theme.SkyTestTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun SkyNewsScreen(
    navigateToStory: ((storyId: String) -> Unit),
    skyNewsUIInteractor: SkyNewsUIInteractor = hiltViewModel<SkyNewsViewModel>()
) {

    val skyTitleState = skyNewsUIInteractor.skyTitle.collectAsStateWithLifecycle()
    val skyNewsListState = skyNewsUIInteractor.skyNewsListStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val uriHandler = LocalUriHandler.current
        Spacer(modifier = Modifier.height(20.dp))
        SkyNewsTitle(titleState = skyTitleState)
        Spacer(modifier = Modifier.height(10.dp))
        SkyNewsList(
            skyNewsListState = skyNewsListState,
            navigateToStory = navigateToStory,
            navigateToWeblink = { weblinkUrl ->
                Result.runCatching {
                    uriHandler.openUri(weblinkUrl)
                }
            },
            navigateToAdvert = { advertUrl ->
                Result.runCatching {
                    uriHandler.openUri(advertUrl)
                }
            }
        )
    }
}

@Composable
private fun SkyNewsTitle(titleState: State<String>) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        text = titleState.value,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        color = Color.Black,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun SkyNewsList(
    modifier: Modifier = Modifier,
    skyNewsListState: State<List<SkyNewsListResponse.SkyNewsObject>>,
    navigateToStory: (storyId: String) -> Unit,
    navigateToWeblink: ((weblink: String) -> Unit),
    navigateToAdvert: ((advertUrl: String) -> Unit)
) {
    val skyItems = skyNewsListState.value
    LazyColumn(modifier = modifier) {
        if (skyItems.isNotEmpty()) {
            item {
                BigSkyNewsItem(
                    skyItem = skyNewsListState.value.first(),
                    navigateToWeblink = navigateToWeblink,
                    navigateToStory = navigateToStory
                )
            }
        }
        if (skyItems.size >= 2) {
            items(skyItems.subList(1, skyItems.size)) {
                SmallSkyNewsItem(
                    skyItem = it,
                    navigateToWeblink = navigateToWeblink,
                    navigateToStory = navigateToStory,
                    navigateToAdvert = navigateToAdvert
                )
            }
            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun BigSkyNewsItem(
    skyItem: SkyNewsListResponse.SkyNewsObject,
    navigateToStory: (storyId: String) -> Unit,
    navigateToWeblink: ((weblink: String) -> Unit)
) {
    val onClickAction: (() -> Unit)? = when (skyItem.type) {
        "weblink" -> {
            { skyItem.weblinkUrl?.let(navigateToWeblink) }
        }

        "story" -> {
            { skyItem.id?.let(navigateToStory) }
        }

        else -> null
    }

    when (skyItem.type) {
        "advert" -> {
            //empty, no big advert
        }

        "weblink",
        "story" -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onClickAction?.invoke() })
            ) {
                BigNewsPhoto(
                    photoUrl = skyItem.teaserImage?._links?.url?.href,
                    contentDescription = skyItem.teaserImage?.accessibilityText
                )
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)) {
                    skyItem.headline?.let { headline ->
                        Text(text = headline, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    skyItem.teaserText?.let { teaserText ->
                        Text(
                            text = teaserText,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    skyItem.creationDate?.let { creationDate ->
                        Text(
                            modifier = Modifier.alpha(0.4f),
                            text = formatPostDate(creationDate)
                        )
                    }

                }
            }
        }
        else -> {
            //empty, unavailable type
        }
    }
}

@Composable
private fun SmallSkyNewsItem(
    skyItem: SkyNewsListResponse.SkyNewsObject,
    navigateToStory: (storyId: String) -> Unit,
    navigateToWeblink: ((weblink: String) -> Unit),
    navigateToAdvert: ((advertUrl: String) -> Unit)
) {

    val onClickAction: (() -> Unit)? = when (skyItem.type) {
        "advert" -> {
            { skyItem.url?.let(navigateToAdvert) }
        }

        "weblink" -> {
            { skyItem.weblinkUrl?.let(navigateToWeblink) }
        }

        "story" -> {
            { skyItem.id?.let(navigateToStory) }
        }

        else -> null
    }

    when (skyItem.type) {
        "advert" -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(GreyPlaceholderBackground)
                    .clickable {
                        skyItem.url?.let(navigateToAdvert)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier,
                    text = "Advert",
                    textAlign = TextAlign.Center
                )
            }
        }

        "weblink",
        "story" -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
                    .clickable(onClick = { onClickAction?.invoke() })
            ) {
                CoilImage(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(64.dp),
                    imageModel = { skyItem.teaserImage?._links?.url?.href },
                    imageOptions = ImageOptions(
                        contentDescription = skyItem.teaserImage?.accessibilityText,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    },
                    failure = {
                        Image(
                            painter = painterResource(id = R.drawable.sky_news_img),
                            contentDescription = skyItem.teaserImage?.accessibilityText
                                ?: "Sky news logo"
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        skyItem.headline?.let { headline ->
                            Text(
                                modifier = Modifier.weight(1f),
                                text = headline,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        skyItem.creationDate?.let { creationDate ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.alpha(0.4f),
                                text = formatPostDate(creationDate)
                            )
                        }
                    }
                    skyItem.teaserText?.let { teaserText ->
                        Text(
                            text = teaserText,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        else -> {
            //empty, unavailable type, to be done in next version of app (if server will add new type)
        }
    }
}

@Composable
fun BigNewsPhoto(
    modifier: Modifier = Modifier,
    photoUrl: String?,
    contentDescription: String?
) {
    val imageModel =
        if (LocalView.current.isInEditMode)
            R.drawable.test_preview_cat_image
        else
            photoUrl
    CoilImage(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        imageModel = { imageModel },
        imageOptions = ImageOptions(
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        ),
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(64.dp)
            )
        },
        failure = {
            Image(
                painter = painterResource(id = R.drawable.sky_news_img),
                contentDescription = contentDescription
            )
        }
    )
}

@Preview
@Composable
private fun SkyNewsPreview() {
    SkyTestTheme {
        val testData = getJsonFromAssets<SkyNewsListResponse>(
            context = LocalContext.current,
            fileName = "json/sample_list_cats.json"
        ).getOrThrow().data
        SkyNewsScreen(
            navigateToStory = {},
            skyNewsUIInteractor = SkyNewsUIInteractor.getTestSkyNewsUIInteractor(testData)
        )
    }
}

@Preview
@Composable
private fun BigSkyNewsItemPreview(@PreviewParameter(SkyNewsTypeProviderPreview::class) previewNewsType: State<String>) {
    SkyTestTheme {
        val testItem = getJsonFromAssets<SkyNewsListResponse>(
            context = LocalContext.current,
            fileName = "json/sample_list_cats.json"
        ).getOrThrow().data.find { it.type == previewNewsType.value }!!
        BigSkyNewsItem(skyItem = testItem, navigateToStory = {}, navigateToWeblink = {})
    }
}

@Preview
@Composable
private fun SmallSkyNewsItemPreview(@PreviewParameter(SkyNewsTypeProviderPreview::class) previewNewsType: State<String>) {
    SkyTestTheme {
        val testItem = getJsonFromAssets<SkyNewsListResponse>(
            context = LocalContext.current,
            fileName = "json/sample_list_cats.json"
        ).getOrThrow().data.find { it.type == previewNewsType.value }!!
        SmallSkyNewsItem(
            skyItem = testItem,
            navigateToWeblink = {},
            navigateToStory = {},
            navigateToAdvert = {})
    }
}

class SkyNewsTypeProviderPreview :
    PreviewParameterProvider<State<String>> {
    override val values: Sequence<State<String>> =
        sequenceOf(
            mutableStateOf("story"),
            mutableStateOf("weblink"),
            mutableStateOf("advert")
        )
}