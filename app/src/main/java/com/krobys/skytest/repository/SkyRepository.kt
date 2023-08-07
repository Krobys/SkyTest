package com.krobys.skytest.repository

import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse

interface SkyRepository {
    suspend fun getNewsList(): Result<SkyNewsListResponse>
    suspend fun getStory(id: String): Result<SkyStoryResponse>
}