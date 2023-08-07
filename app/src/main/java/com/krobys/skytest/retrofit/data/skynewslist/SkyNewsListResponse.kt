package com.krobys.skytest.retrofit.data.skynewslist

data class SkyNewsListResponse(
    val `data`: List<SkyNewsObject>,
    val title: String
) {
    data class SkyNewsObject(
        val creationDate: String?,
        val headline: String?,
        val id: String?,
        val modifiedDate: String?,
        val teaserImage: TeaserImage?,
        val teaserText: String?,
        val type: String,
        val url: String?,
        val weblinkUrl: String?
    ) {
        data class TeaserImage(
            val _links: Links,
            val accessibilityText: String
        ) {
            data class Links(
                val url: Url
            ) {
                data class Url(
                    val href: String,
                    val templated: Boolean,
                    val type: String
                )
            }
        }
    }
}