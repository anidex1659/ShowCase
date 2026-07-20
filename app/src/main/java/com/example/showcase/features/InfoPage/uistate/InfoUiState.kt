package com.example.showcase.features.InfoPage.uistate

import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.series.PlaybackHistoryEntity
import com.example.showcase.features.MetaData.data.model.EpisodeUiModel
import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesMetadata
import com.example.showcase.features.MetaData.data.model.SeriesUiModel

data class InfoUiState(

    val loading: Boolean = false,

    val error: String? = null,

    val series: SeriesInfoUiModel? = null,

    val movie: MovieInfoUiModel? = null
)


data class SeriesInfoUiModel(

    val media: SeriesUiModel,

    val metadata: SeriesMetadata?,

    val playback: PlaybackHistoryEntity?,

    // Flat episode list for player/EpisodePanel
    val episodes: List<EpisodeUiModel> = emptyList(),

    val recommendations: List<SeriesUiModel> = emptyList(),

    val similar: List<SeriesUiModel> = emptyList()
)
data class MovieInfoUiModel(

    val movie: MovieUiModel,

    val metadata: MovieMetadata?,

    val playback: MoviePlaybackEntity?,

    val recommendations: List<MovieUiModel> = emptyList(),

    val similar: List<MovieUiModel> = emptyList()
)