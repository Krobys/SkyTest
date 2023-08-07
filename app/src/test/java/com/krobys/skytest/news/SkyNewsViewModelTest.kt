package com.krobys.skytest.news

import com.krobys.skytest.repository.SkyRepository
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse
import com.krobys.skytest.screens.skynews.SkyNewsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SkyNewsViewModelTest {

    @Mock
    private lateinit var skyRepository: SkyRepository

    private val dispatcher = StandardTestDispatcher()

    private val testSkyNewsListResponse = SkyNewsListResponse(
        data = listOf(),
        title = "Sky Cat News"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when requestNewsList is called, then news list and title are retrieved`() = runTest {
        `when`(skyRepository.getNewsList()).thenReturn(Result.success(testSkyNewsListResponse))

        val viewModel = SkyNewsViewModel(skyRepository)

        viewModel.requestNewsList()
        delay(10)
        assertEquals(testSkyNewsListResponse.data, viewModel.skyNewsListStateFlow.value)
        assertEquals(testSkyNewsListResponse.title, viewModel.skyTitle.value)
    }

    @Test
    fun `when requestNewsList fails, state remains unchanged`() = runTest {
        `when`(skyRepository.getNewsList()).thenReturn(Result.failure(Exception("Fetch failed")))

        val viewModel = SkyNewsViewModel(skyRepository)

        viewModel.requestNewsList()
        delay(10)
        assertEquals(listOf<SkyNewsListResponse.SkyNewsObject>(), viewModel.skyNewsListStateFlow.value)
        assertEquals("", viewModel.skyTitle.value)
    }
}
