package com.krobys.skytest.story

import androidx.lifecycle.SavedStateHandle
import com.krobys.skytest.repository.SkyRepository
import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import com.krobys.skytest.screens.skystory.SkyStoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SkyStoryViewModelTest {

    @Mock
    private lateinit var skyRepository: SkyRepository

    private val storyId = "testStoryId"

    private val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
        this["storyId"] = storyId
    }

    private val testSkyStoryResponse = SkyStoryResponse(
        contents = listOf(),
        headline = "headline 1",
        creationDate = "creation date",
        heroImage = SkyStoryResponse.HeroImage("", ""),
        id = "testStoryId",
        modifiedDate = "modified date"
    )

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when story id is received, then story is retrieved`() = runTest {
        `when`(skyRepository.getStory(storyId)).thenReturn(Result.success(testSkyStoryResponse))

        val viewModel = SkyStoryViewModel(skyRepository, savedStateHandle)

        viewModel.skyStoryStateFlow.value?.let {
            assertEquals(testSkyStoryResponse, it)
        }
    }

    @Test
    fun `when story retrieval fails, state remains unchanged`() = runTest {
        `when`(skyRepository.getStory(storyId)).thenReturn(Result.failure(Exception("Fetch failed")))

        val viewModel = SkyStoryViewModel(skyRepository, savedStateHandle)

        viewModel.skyStoryStateFlow.value?.let {
            assertEquals(null, it)
        }
    }

}
