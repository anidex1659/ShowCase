package com.example.showcase.features.HomeScreen.repo

import com.example.showcase.features.HomeScreen.model.HeroUiModel
import com.example.showcase.features.HomeScreen.model.RecentPlaybackUiModel
import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository

class HomeRepository(
    private val seriesRepository: SeriesRepository,
    private val movieRepository: MovieRepository
) {
    suspend fun getRecentlyPlayed(): RecentPlaybackUiModel? {

        val latestSeries = seriesRepository
            .getSeries()
            .filter {
                it.lastPlayedEpisode != null
            }
            .maxByOrNull {
                it.lastPlayedEpisode?.lastPlayed ?: 0L
            }

        val latestMovie = movieRepository
            .getRecentlyPlayed()

        val seriesTime =
            latestSeries
                ?.lastPlayedEpisode
                ?.lastPlayed
                ?: 0L

        val movieTime =
            latestMovie
                ?.playback
                ?.lastPlayed
                ?: 0L

        return when {

            latestSeries == null && latestMovie == null -> {
                null
            }

            movieTime > seriesTime -> {
                RecentPlaybackUiModel.Movie(
                    latestMovie!!
                )
            }

            else -> {
                RecentPlaybackUiModel.Series(
                    latestSeries!!
                )
            }
        }
    }
    suspend fun getContinueWatching(): List<SeriesUiModel> {

        return seriesRepository
            .getSeries()
            .filter {

                it.lastPlayedEpisode != null
            }
            .sortedByDescending {

                it.lastPlayedEpisode?.lastPlayed ?: 0L
            }
    }

    suspend fun getRecentlyAddedSeries(): List<SeriesUiModel> {

        return seriesRepository
            .getRecentlyAdded(20)
    }

    suspend fun getRecentlyAddedMovies(): List<MovieUiModel> {

        return movieRepository
            .getRecentlyAdded(20)
    }

    suspend fun getTrendingSeries(): List<SeriesUiModel> {

        // Temporary
        return seriesRepository
            .getRecentlyAdded(10)
    }

    suspend fun getTrendingMovies(): List<MovieUiModel> {

        // Temporary
        return movieRepository
            .getRecentlyAdded(10)
    }

    suspend fun getHeroItems(): List<HeroUiModel> {

        val series = seriesRepository
            .getRecentlyAdded(5)
            .map {
                HeroUiModel.Series(it)
            }

        val movies = movieRepository
            .getRecentlyAdded(5)
            .map {
                HeroUiModel.Movie(it)
            }

        return (series + movies)
            .shuffled()
            .take(7)
    }
}