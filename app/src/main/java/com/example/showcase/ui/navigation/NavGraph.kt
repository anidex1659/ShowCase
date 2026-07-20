package com.example.showcase.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.showcase.features.HomeScreen.HomeScreen
import com.example.showcase.features.InfoPage.InfoPage
import com.example.showcase.features.Library.ui.LibraryScreen
import com.example.showcase.features.MediaPages.MediaPage
//import com.example.showcase.features.MetaData.data.scanner.ScannerTestScreen
import com.example.showcase.features.Player.ui.PlayScreenV
import com.example.showcase.features.SplashScreen.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {

        composable<SplashRoute> {
            SplashScreen(navController)
        }

        composable<HomeRoute> {
            HomeScreen(navController)
        }

        composable<LibraryRoute> {
            LibraryScreen()
        }

        composable<MediaRoute> { backStack ->

            val route = backStack.toRoute<MediaRoute>()

            MediaPage(
                navController = navController,
                Show = route.Show
            )
        }


        composable<PlayerRoute> { backStack ->

            val route = backStack.toRoute<PlayerRoute>()

            PlayScreenV(
                navController = navController,
                mediaId = route.id,
                mediaType = route.type
            )
        }

        composable<MovieInfoRoute> { backStack ->

            val route =
                backStack.toRoute<MovieInfoRoute>()

            InfoPage(
                movieId = route.movieId,
                navController=navController
            )
        }

        composable<SeriesInfoRoute> { backStack ->

            val route =
                backStack.toRoute<SeriesInfoRoute>()

            InfoPage(
                seriesId = route.seriesId,
                navController=navController
            )
        }

    }
}