package com.example.showcase.features.MetaData.data.remote

import com.example.showcase.BuildConfig
import com.example.showcase.features.MetaData.data.remote.api.TmdbApi
import com.example.showcase.features.MetaData.data.remote.model.TmdbImagesResponse
import com.example.showcase.features.MetaData.data.remote.model.TmdbMovieDetails
import com.example.showcase.features.MetaData.data.remote.model.TmdbMovieResult
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeasonDetails
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeriesDetails
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeriesResult


class TmdbRemoteDataSource(

    private val api: TmdbApi
) {

    companion object {

        private const val IMAGE_BASE =
            "https://image.tmdb.org/t/p/original"
    }

    suspend fun searchSeries(
        title: String
    ): List<TmdbSeriesResult> {

        return api.searchSeries(title).results
    }



    suspend fun getSeriesDetails(
        id: Int
    ): TmdbSeriesDetails {

        return api.getSeriesDetails(id)
    }

    suspend fun getSeasonDetails(
        tvId: Int,
        seasonNumber: Int
    ): TmdbSeasonDetails {

        return api.getSeasonDetails(
            tvId = tvId,
            seasonNumber = seasonNumber,
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }

    suspend fun getSeriesImages(
        tvId: Int
    ): TmdbImagesResponse {

        return api.getSeriesImages(
            tvId = tvId,
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }

    fun buildImageUrl(
        path: String?
    ): String? {

        return path?.let {
            "$IMAGE_BASE$it"
        }
    }

    suspend fun searchMovie(
        title: String
    ): List<TmdbMovieResult> {

        return api.searchMovie(title).results
    }

    suspend fun getMovieDetails(
        movieId: Int
    ): TmdbMovieDetails {

        return api.getMovieDetails(movieId)
    }

    suspend fun getMovieImages(
        movieId: Int
    ): TmdbImagesResponse {

        return api.getMovieImages(
            movieId = movieId,
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }
}


