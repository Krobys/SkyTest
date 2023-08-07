package com.krobys.skytest.retrofit

import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse
import retrofit2.http.GET

interface SkyNewsApi {
    @GET("/news-list")
    suspend fun getNewsList(): Result<SkyNewsListResponse>

    @GET("/story/{id}")
    suspend fun getStory(id: String): Result<SkyStoryResponse>
}
