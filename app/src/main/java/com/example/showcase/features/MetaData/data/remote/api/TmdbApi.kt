package com.example.showcase.features.MetaData.data.remote.api

import com.example.showcase.BuildConfig
import com.example.showcase.features.MetaData.data.remote.model.TmdbImagesResponse
import com.example.showcase.features.MetaData.data.remote.model.TmdbMovieDetails
import com.example.showcase.features.MetaData.data.remote.model.TmdbMovieSearchResponse
import com.example.showcase.features.MetaData.data.remote.model.TmdbSearchResponse
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeasonDetails
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeriesDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("search/tv")
    suspend fun searchSeries(
        @Query("query")
        query: String
    ): TmdbSearchResponse

    @GET("tv/{id}")
    suspend fun getSeriesDetails(
        @Path("id")
        id: Int
    ): TmdbSeriesDetails

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonDetails(

        @Path("tv_id")
        tvId: Int,

        @Path("season_number")
        seasonNumber: Int,

        @Query("api_key")
        apiKey: String

    ): TmdbSeasonDetails

    @GET("tv/{tv_id}/images")
    suspend fun getSeriesImages(

        @Path("tv_id")
        tvId: Int,

        @Query("api_key")
        apiKey: String
    ): TmdbImagesResponse


    @GET("search/movie")
    suspend fun searchMovie(

        @Query("query")
        title: String,

        @Query("api_key")
        apiKey: String = BuildConfig.TMDB_API_KEY

    ): TmdbMovieSearchResponse


    @GET("movie/{id}")
    suspend fun getMovieDetails(

        @Path("id")
        movieId: Int,

        @Query("api_key")
        apiKey: String = BuildConfig.TMDB_API_KEY

    ): TmdbMovieDetails

    @GET("movie/{id}/images")
    suspend fun getMovieImages(

        @Path("id")
        movieId: Int,

        @Query("api_key")
        apiKey: String = BuildConfig.TMDB_API_KEY,

        @Query("include_image_language")
        languages: String = "en,null"

    ): TmdbImagesResponse
}