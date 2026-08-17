package com.example.showcase.features.HomeScreen.model

import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.ui.Navigation.PlayerType
import java.util.Optional.empty

data class HomeUiState(

    val loading: Boolean = false,

    val hero: List<HeroUiModel> = emptyList(),

    val continueWatching: List<SeriesUiModel> = emptyList(),

    val trendingSeries: List<SeriesUiModel> = emptyList(),

    val trendingMovies: List<MovieUiModel> = emptyList(),

    val recentlyAddedSeries: List<SeriesUiModel> = emptyList(),
    val recentlyAddedMovies: List<MovieUiModel> = emptyList(),
    val error: String? = null
)

data class ContinueWatchingItem(
    val id: Long,
    val type: PlayerType,
    val title: String,
    val backdrop: String?,
    val progress: Float = 0f
)

sealed class HeroUiModel {

    data class Series(
        val item: SeriesUiModel
    ) : HeroUiModel()

    data class Movie(
        val item: MovieUiModel
    ) : HeroUiModel()
}


sealed class RecentPlaybackUiModel {

    data class Series(
        val series: SeriesUiModel
    ) : RecentPlaybackUiModel()

    data class Movie(
        val movie: MovieUiModel
    ) : RecentPlaybackUiModel()
}