package com.krobys.skytest.repository

import com.krobys.skytest.retrofit.SkyNewsApi
import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse
import javax.inject.Inject

class SkyRepositoryImpl @Inject constructor(
    private val skyApi: SkyNewsApi
) : SkyRepository {

    override suspend fun getNewsList(): Result<SkyNewsListResponse> {
        //the layer is needed because caching can be added in the future
        return skyApi.getNewsList()
    }

    override suspend fun getStory(id: String): Result<SkyStoryResponse> {
        //the layer is needed because caching can be added in the future
        return skyApi.getStory(id)
    }
}