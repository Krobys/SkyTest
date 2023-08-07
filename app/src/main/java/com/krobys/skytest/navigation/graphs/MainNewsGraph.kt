package com.krobys.skytest.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.krobys.skytest.navigation.Destinations
import com.krobys.skytest.screens.skynews.SkyNewsScreen
import com.krobys.skytest.screens.skystory.SkyStoryScreen

fun NavGraphBuilder.mainNewsGraph(
    navController: NavController
) {
    composable(Destinations.SkyNews.route) {
        SkyNewsScreen(
            navigateToStory = { storyId ->
                navController.navigate("${Destinations.Story.route}/$storyId")
            })
    }
    composable("${Destinations.Story.route}/{storyId}", arguments = listOf(navArgument("storyId") { type = NavType.StringType })) { backStackEntry ->
        SkyStoryScreen()
    }
}

