package com.krobys.skytest.navigation

sealed class Destinations(val route: String) {
    object SkyNews : Destinations("sky-news")
    object Story : Destinations("story")
}
