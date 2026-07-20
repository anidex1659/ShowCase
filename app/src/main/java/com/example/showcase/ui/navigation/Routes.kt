package com.example.showcase.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
object SplashRoute







@Serializable
object HomeRoute


@Serializable
data class MediaRoute(
    val Show: String
)








@Serializable
object LibraryRoute
@Serializable  //is an test replaesment for dats class SeriesDetailsRoute
object InfoRoute
@Serializable //is an test replaesment for dats class Player
object PlayerV



@Serializable
object SearchRoute
@Serializable
object ScannerTestRoute
@Serializable
object SettingsRoute

@Serializable
data class MovieDetailsRoute(
    val movieId: Long
)

@Serializable
data class MovieInfoRoute(
    val movieId: Long
)


@Serializable
data class SeriesInfoRoute(
    val seriesId: Long
)


@Serializable
enum class PlayerType {
    MOVIE,
    SERIES
}

@Serializable
data class PlayerRoute(
    val id: Long,
    val type: PlayerType
)





@Serializable
data class SeriesDetailsRoute(
    val seriesId: Long
)