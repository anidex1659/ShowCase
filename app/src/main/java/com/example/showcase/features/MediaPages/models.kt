package com.example.showcase.features.MediaPages

import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel

data class MediaCardUiModel(
    val id: Long,
    val title: String,
    val poster: String?,
    val backdrop: String?,
    val subtitle: String?,
    val type: MediaType,

    // Metadata
    val plot: String? = null,
    val tagline: String? = null,
    val genres: List<String> = emptyList(),
    val rating: Float? = null,
    val year: String? = null,
    val runtime: Int? = null
)
data class MediaUiState(

    val loading: Boolean = true,

    val media: List<MediaCardUiModel> = emptyList(),

    val hero: MediaCardUiModel? = null,
    val heroInfo: String = "",

    val selectedType: MediaType = MediaType.MOVIE
)
enum class MediaType {
    MOVIE,
    SERIES
}

fun MovieUiModel.toMediaCard() = MediaCardUiModel(
    id = movie.id,
    title = movie.title,
    poster = artwork.firstOrNull { it.type == "poster" }?.uri,
    backdrop = artwork.firstOrNull { it.type == "backdrop" }?.uri,
    subtitle = movie.duration.toString(),
    type = MediaType.MOVIE
)

fun SeriesUiModel.toMediaCard() = MediaCardUiModel(
    id = series.id,
    title = series.title,
    poster = poster?.uri,
    backdrop = backdrop?.uri,
    subtitle = "${series.episodeCount} Episodes",
    type = MediaType.SERIES
)