package com.example.showcase.features.MetaData.data.model


import kotlinx.serialization.Serializable

@Serializable
data class SeriesMetadata(

    val title: String = "",
    val originalTitle: String = "",
    val plot: String = "",
    val rating: Float = 0f,
    val year: Int = 0,
    val status: String = "",
    val genres: List<String> = emptyList(),
    val studios: List<String> = emptyList(),
    val cast: List<String> = emptyList(),
    val seasons: List<SeasonMetadata> = emptyList()
)

@Serializable
data class SeasonMetadata(
    val seasonNumber: Int,
    val episodes: List<EpisodeMetadata>
)

@Serializable
data class EpisodeMetadata(
    val episodeNumber: Int,
    val title: String,
    val plot: String = "",
    val runtime: Int = 0
)


@Serializable
data class MovieMetadata(

    val title: String,
    val originalTitle: String,
    val plot: String,
    val tagline: String = "",
    val runtime: Int,
    val rating: Double,
    val voteCount: Int,
    val year: Int,
    val releaseDate: String,
    val status: String,
    val language: String,
    val genres: List<String>,
    val studios: List<String>,
    val countries: List<String>,
    val budget: Long,
    val revenue: Long,
    val imdbId: String? = null

)