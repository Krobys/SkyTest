package com.krobys.skytest.repository

import android.app.Application
import com.google.gson.Gson
import com.krobys.skytest.retrofit.SkyNewsApi
import com.krobys.skytest.retrofit.data.skyStory.SkyStoryResponse
import com.krobys.skytest.retrofit.data.skynewslist.SkyNewsListResponse
import com.krobys.skytest.tools.AssetsTool.getJsonFromAssets
import javax.inject.Inject

class SkyNewsApiTestImpl @Inject constructor(
    private val application: Application,
    private val gson: Gson
) : SkyNewsApi {

    override suspend fun getNewsList(): Result<SkyNewsListResponse> {
        return getJsonFromAssets(
            context = application,
            fileName = "json/sample_list_cats.json",
            gson = gson
        )
    }

    override suspend fun getStory(id: String): Result<SkyStoryResponse> { //returns story for id or story with id 1 if id is not present in stories
        val storyResult: Result<SkyStoryResponse> = getJsonFromAssets(
            context = application,
            fileName = "json/stories/sample_story${id}.json",
            gson = gson
        )

        return if (storyResult.isSuccess) storyResult else getJsonFromAssets(
            context = application,
            fileName = "json/stories/sample_story1.json",
            gson = gson
        )
    }

}