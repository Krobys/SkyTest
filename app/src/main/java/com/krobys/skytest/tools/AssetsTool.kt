package com.krobys.skytest.tools

import android.content.Context
import com.google.gson.Gson

object AssetsTool {
    inline fun <reified T> getJsonFromAssets(
        context: Context,
        fileName: String,
        gson: Gson = Gson()
    ): Result<T> {
        return runCatching {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(jsonString, T::class.java)
        }
    }
}