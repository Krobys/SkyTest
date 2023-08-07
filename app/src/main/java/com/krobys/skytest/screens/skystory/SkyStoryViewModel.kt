package com.krobys.skytest.screens.skystory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krobys.skytest.manager.MessageManager
import com.krobys.skytest.repository.SkyRepository
import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkyStoryViewModel @Inject constructor(
    private val skyRepository: SkyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), SkyStoryUIInteractor {

    private val _skyStoryStateFlow = MutableStateFlow<SkyStoryResponse?>(null)
    override val skyStoryStateFlow: StateFlow<SkyStoryResponse?> = _skyStoryStateFlow

    init {
        val storyId = savedStateHandle.get<String>("storyId")
        requestStory(storyId)
    }

    private fun requestStory(storyId: String?) {
        viewModelScope.launch {
            storyId?.let {
                skyRepository.getStory(it)
                    .onSuccess { response ->
                        _skyStoryStateFlow.value = response
                    }.onFailure {
                        MessageManager.postError("Sorry, story currently unavailable")
                    }
            }
        }
    }
}


interface SkyStoryUIInteractor {
    val skyStoryStateFlow: StateFlow<SkyStoryResponse?>

    companion object {
        fun getTestSkyStoryUIInteractor(response: SkyStoryResponse? = null) =
            object : SkyStoryUIInteractor {
                override val skyStoryStateFlow: StateFlow<SkyStoryResponse?> =
                    MutableStateFlow(response)

            }
    }
}