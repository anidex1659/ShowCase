package com.example.showcase.features.MetaData.data.repository.metadata.movie

import android.util.Log
import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.remote.model.MovieMetadataFetchResult

class MovieMetadataRepository(

    private val remote: TmdbRemoteDataSource

) {

    suspend fun fetchMovieMetadata(

        title: String

    ): MovieMetadataFetchResult? {

        Log.d(
            "Anidex_Metadata",
            "Fetching movie metadata for $title"
        )

        val searchResults =
            remote.searchMovie(title)

        Log.d(
            "Anidex_Metadata",
            "Results = ${searchResults.size}"
        )

        searchResults.forEach {

            Log.d(

                "Anidex_Metadata",

                "${it.id} -> ${it.title} (${it.releaseDate})"

            )

        }

        if (searchResults.isEmpty())
            return null

        val bestMatch =
            searchResults.first()

        val details =
            remote.getMovieDetails(
                bestMatch.id
            )

        val images =
            remote.getMovieImages(
                bestMatch.id
            )

        val logo =
            images.logos
                .firstOrNull()
                ?.filePath

        val metadata =

            MovieMetadata(

                title = details.title,

                originalTitle = details.originalTitle,

                plot = details.overview,

                tagline = details.tagline,

                runtime = details.runtime,

                rating = details.voteAverage,

                voteCount = details.voteCount,

                year = extractYear(
                    details.releaseDate
                ),

                releaseDate = details.releaseDate,

                status = details.status,

                language = details.originalLanguage,

                genres =
                    details.genres.map {
                        it.name
                    },

                studios =
                    details.productionCompanies.map {
                        it.name
                    },

                countries =
                    details.productionCountries.map {
                        it.name
                    },

                budget = details.budget,

                revenue = details.revenue,

                imdbId = details.imdbId

            )

        Log.d(

            "Anidex_Metadata",

            "Movie Metadata Ready : ${details.title}"

        )

        return MovieMetadataFetchResult(

            metadata = metadata,

            movieId = bestMatch.id,

            posterPath = bestMatch.posterPath,

            backdropPath = bestMatch.backdropPath,

            logoPath = logo

        )
    }

    private fun extractYear(
        date: String
    ): Int {

        return date
            .take(4)
            .toIntOrNull()
            ?: 0
    }
}