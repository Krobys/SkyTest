package com.krobys.skytest.screens.skynews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krobys.skytest.manager.MessageManager
import com.krobys.skytest.repository.SkyRepository
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkyNewsViewModel @Inject constructor(
    private val skyRepository: SkyRepository
) : ViewModel(), SkyNewsUIInteractor {

    private val _skyNewsListStateFlow =
        MutableStateFlow<List<SkyNewsListResponse.SkyNewsObject>>(listOf())
    override val skyNewsListStateFlow: StateFlow<List<SkyNewsListResponse.SkyNewsObject>> =
        _skyNewsListStateFlow

    private val _skyTitle = MutableStateFlow("")
    override val skyTitle: StateFlow<String> = _skyTitle

    init {
        viewModelScope.launch {
            requestNewsList()
        }
    }

    override fun requestNewsList() {
        viewModelScope.launch {
            val newsList = skyRepository.getNewsList()
            newsList.onSuccess { response ->
                _skyNewsListStateFlow.value = response.data
                _skyTitle.value = response.title
            }.onFailure {
                MessageManager.postError(it)
            }
        }
    }
}

interface SkyNewsUIInteractor {
    val skyNewsListStateFlow: StateFlow<List<SkyNewsListResponse.SkyNewsObject>>
    val skyTitle: StateFlow<String>

    fun requestNewsList()

    companion object {
        fun getTestSkyNewsUIInteractor(newsList: List<SkyNewsListResponse.SkyNewsObject> = listOf()) =
            object : SkyNewsUIInteractor {
                override val skyNewsListStateFlow: StateFlow<List<SkyNewsListResponse.SkyNewsObject>> =
                    MutableStateFlow(newsList)
                override val skyTitle: StateFlow<String> = MutableStateFlow("Sky Cat News")

                override fun requestNewsList() {

                }
            }
    }
}