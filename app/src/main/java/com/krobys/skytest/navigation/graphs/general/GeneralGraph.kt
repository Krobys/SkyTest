package com.krobys.skytest.navigation.graphs.general

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.krobys.skytest.navigation.Destinations
import com.krobys.skytest.navigation.graphs.mainNewsGraph

@Composable
fun GeneralGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SkyNews.route,
        modifier = modifier,
    ) {
       mainNewsGraph(navController)
    }
}

fun NavController.navigateBack() {
    if(popBackStack().not()) {
        //finish()
    }
}