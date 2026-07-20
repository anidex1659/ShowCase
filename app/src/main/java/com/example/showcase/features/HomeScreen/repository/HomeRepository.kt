package com.example.showcase.features.HomeScreen.repo

import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository

class HomeRepository(

    private val seriesRepository: SeriesRepository,

    private val movieRepository: MovieRepository

) {

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

    suspend fun getHeroItems(): List<SeriesUiModel> {

        return seriesRepository
            .getRecentlyAdded(limit = 5)


    }
}