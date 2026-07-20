package com.example.showcase.features.MetaData.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbSearchResponse(
    val results: List<TmdbSeriesResult>
)

@Serializable
data class TmdbSeriesResult(
    val id: Int,
    val name: String,
    val overview: String = "",
    @SerialName("first_air_date")
    val firstAirDate: String = "",
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null
)

@Serializable
data class TmdbSeriesDetails(
    val id: Int,

    val name: String,

    @SerialName("original_name")
    val originalName: String,

    val overview: String,

    @SerialName("vote_average")
    val voteAverage: Float,

    @SerialName("first_air_date")
    val firstAirDate: String,

    val genres: List<TmdbGenre> = emptyList(),

    val seasons: List<TmdbSeason> = emptyList()
)

@Serializable
data class TmdbGenre(
    val id: Int,
    val name: String
)
@Serializable
data class TmdbSeasonDetails(

    @SerialName("season_number")
    val seasonNumber: Int,

    val episodes: List<TmdbEpisode>
)



@Serializable
data class TmdbImagesResponse(

    val backdrops: List<TmdbImage> = emptyList(),

    val posters: List<TmdbImage> = emptyList(),

    val logos: List<TmdbImage> = emptyList()
)


@Serializable
data class TmdbMovieResult(

    val id: Int,

    val title: String,

    @SerialName("original_title")
    val originalTitle: String,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("release_date")
    val releaseDate: String = ""

)


@Serializable
data class TmdbMovieSearchResponse(

    val results: List<TmdbMovieResult>

)


@Serializable
data class TmdbMovieDetails(

    val id: Int,

    val title: String,

    @SerialName("original_title")
    val originalTitle: String,

    val overview: String,

    val tagline: String,

    val runtime: Int,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int,

    @SerialName("release_date")
    val releaseDate: String,

    val status: String,

    @SerialName("original_language")
    val originalLanguage: String,

    val budget: Long,

    val revenue: Long,

    @SerialName("imdb_id")
    val imdbId: String?,

    val genres: List<TmdbGenre>,

    @SerialName("production_companies")
    val productionCompanies: List<TmdbProductionCompany>,

    @SerialName("production_countries")
    val productionCountries: List<TmdbProductionCountry>

)


@Serializable
data class TmdbProductionCompany(

    val id: Int,

    val name: String

)


@Serializable
data class TmdbProductionCountry(

    @SerialName("iso_3166_1")
    val code: String,

    val name: String

)






@Serializable
data class TmdbImage(

    @SerialName("file_path")
    val filePath: String
)
@Serializable
data class TmdbEpisode(

    @SerialName("episode_number")
    val episodeNumber: Int,

    val name: String,

    val overview: String = "",

    val runtime: Int? = 0
)
@Serializable
data class TmdbSeason(
    @SerialName("season_number")
    val seasonNumber: Int,

    @SerialName("episode_count")
    val episodeCount: Int
)