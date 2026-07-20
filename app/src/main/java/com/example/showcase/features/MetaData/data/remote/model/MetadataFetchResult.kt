package com.example.showcase.features.MetaData.data.remote.model

import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.model.SeriesMetadata



data class MetadataFetchResult(

    val metadata: SeriesMetadata,

    val tvId: Int,

    val posterPath: String?,

    val backdropPath: String?,

    val logoPath: String?
)


data class MovieMetadataFetchResult(

    val metadata: MovieMetadata,

    val movieId: Int,

    val posterPath: String?,

    val backdropPath: String?,

    val logoPath: String?

)