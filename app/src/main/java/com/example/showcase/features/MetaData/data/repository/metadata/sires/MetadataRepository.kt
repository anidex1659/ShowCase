package com.example.showcase.features.MetaData.data.repository.metadata.sires

import android.util.Log
import com.example.showcase.features.MetaData.data.model.EpisodeMetadata
import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.model.SeasonMetadata
import com.example.showcase.features.MetaData.data.model.SeriesMetadata
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.remote.model.MetadataFetchResult
import com.example.showcase.features.MetaData.data.remote.model.MovieMetadataFetchResult

class MetadataRepository(

    private val remote: TmdbRemoteDataSource

) {

    suspend fun fetchSeriesMetadata(
        title: String
    ): MetadataFetchResult? {

        Log.d(
            "Anidex_Metadata",
            "Fetching Metadata for $title"
        )


        val searchResults =
            remote.searchSeries(title)

        if (searchResults.isEmpty()) {
            return null
        }

        val bestMatch =
            searchResults.first()

        val details =
            remote.getSeriesDetails(
                bestMatch.id
            )

        val images =
            remote.getSeriesImages(
                bestMatch.id
            )

        val logoPath =
            images.logos
                .firstOrNull()
                ?.filePath

        val seasonsMetadata =
            details.seasons
                .filter {
                    it.seasonNumber > 0
                }
                .map { season ->

                    val seasonDetails =
                        remote.getSeasonDetails(

                            tvId =
                                bestMatch.id,

                            seasonNumber =
                                season.seasonNumber
                        )

                    SeasonMetadata(

                        seasonNumber =
                            season.seasonNumber,

                        episodes =
                            seasonDetails.episodes.map {

                                EpisodeMetadata(

                                    episodeNumber =
                                        it.episodeNumber,

                                    title =
                                        it.name,

                                    plot =
                                        it.overview,

                                    runtime =
                                        it.runtime ?: 0
                                )
                            }
                    )
                }

        val metadata =
            SeriesMetadata(

                title =
                    details.name,

                originalTitle =
                    details.originalName,

                plot =
                    details.overview,

                rating =
                    details.voteAverage,

                year =
                    extractYear(
                        details.firstAirDate
                    ),

                genres =
                    details.genres.map {
                        it.name
                    },

                seasons =
                    seasonsMetadata
            )

        Log.d(
            "Anidex_Metadata",
            "Metadata Ready : ${details.name}"
        )

        return MetadataFetchResult(

            metadata =
                metadata,

            tvId =
                bestMatch.id,

            posterPath =
                bestMatch.posterPath,

            backdropPath =
                bestMatch.backdropPath,

            logoPath =
                logoPath
        )
    }

    suspend fun fetchMetadataByTmdbId(
        tmdbId: Int
    ): MetadataFetchResult? {

        val details =
            remote.getSeriesDetails(
                tmdbId
            )

        val images =
            remote.getSeriesImages(
                tmdbId
            )

        val logo =
            images.logos.firstOrNull()?.filePath

        val seasonsMetadata =
            details.seasons
                .filter {
                    it.seasonNumber > 0
                }
                .map { season ->

                    val seasonDetails =
                        remote.getSeasonDetails(
                            tmdbId,
                            season.seasonNumber
                        )

                    SeasonMetadata(

                        seasonNumber =
                            season.seasonNumber,

                        episodes =
                            seasonDetails.episodes.map {

                                EpisodeMetadata(

                                    episodeNumber =
                                        it.episodeNumber,

                                    title =
                                        it.name,

                                    plot =
                                        it.overview,

                                    runtime =
                                        it.runtime ?: 0
                                )
                            }
                    )
                }

        return MetadataFetchResult(

            metadata =

                SeriesMetadata(

                    title =
                        details.name,

                    originalTitle =
                        details.originalName,

                    plot =
                        details.overview,

                    rating =
                        details.voteAverage,

                    year =
                        extractYear(
                            details.firstAirDate
                        ),

                    genres =
                        details.genres.map {
                            it.name
                        },

                    seasons =
                        seasonsMetadata
                ),

            tvId = tmdbId,

            posterPath =
                images.posters.firstOrNull()?.filePath,

            backdropPath =
                images.backdrops.firstOrNull()?.filePath,

            logoPath =
                logo
        )
    }


    suspend fun fetchMovieMetadata(

        title: String

    ): MovieMetadataFetchResult? {

        Log.d(
            "Anidex_Metadata",
            "Fetching movie metadata for $title"
        )

        val searchResults =
            remote.searchMovie(title)

        Log.d("Anidex_Metadata", "Results = ${searchResults.size}")

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
            remote.getMovieDetails(bestMatch.id)

        val images =
            remote.getMovieImages(bestMatch.id)

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