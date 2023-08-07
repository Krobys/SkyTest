package com.krobys.skytest.retrofit.data.skyStory

data class SkyStoryResponse(
    val contents: List<Content>,
    val creationDate: String,
    val headline: String,
    val heroImage: HeroImage,
    val id: String,
    val modifiedDate: String
) {
    data class Content(
        val accessibilityText: String?,
        val text: String?,
        val type: String,
        val url: String?
    )

    data class HeroImage(
        val accessibilityText: String,
        val imageUrl: String
    )
}