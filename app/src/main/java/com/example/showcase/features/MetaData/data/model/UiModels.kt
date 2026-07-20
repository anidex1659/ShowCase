package com.example.showcase.features.MetaData.data.model

import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.series.EpisodeEntity
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.series.PlaybackHistoryEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

data class LibraryUiModel(

    val library: LibraryEntity,

    val series: List<SeriesUiModel> = emptyList(),

    val movies: List<MovieUiModel> = emptyList(),

    val totalItems: Int = 0,

    val recentlyAdded: Long = 0
)

data class SeriesUiModel(

    val series: SeriesEntity,

    val seasons: List<SeasonUiModel>,

    val poster: ArtworkEntity? = null,

    val logo: ArtworkEntity? = null,

    val backdrop: ArtworkEntity? = null,

    val episodeCount: Int = 0,

    val watchedEpisodes: Int = 0,

    val lastPlayedEpisode: EpisodeEntity? = null
)

data class SeasonUiModel(

    val seasonNumber: Int,

    val episodes: List<EpisodeUiModel>,

    val episodeCount: Int = episodes.size
)

data class EpisodeUiModel(

    val episode: EpisodeEntity,

    val playback: PlaybackHistoryEntity? = null,

    val watched: Boolean = false,

    val progressPercent: Float = 0f
)


data class MovieUiModel(

    val movie: MovieEntity,

    val artwork: List<MovieArtworkEntity>,

    val playback: MoviePlaybackEntity?

)