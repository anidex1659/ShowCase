package com.example.showcase.features.InfoPage.repository

import android.util.Log
import com.example.showcase.features.InfoPage.uistate.MovieInfoUiModel
import com.example.showcase.features.InfoPage.uistate.SeriesInfoUiModel
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.model.SeriesMetadata
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository
import kotlinx.serialization.json.Json
import java.io.File

class InfoRepository(

    private val dao: ShowcaseDao,

    private val seriesRepository: SeriesRepository,

    private val movieRepository: MovieRepository,

    private val metadataStorageRepository: MetadataStorageRepository
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun loadSeries(
        seriesId: Long
    ): SeriesInfoUiModel {

        val media =
            seriesRepository.getSeriesById(seriesId)
                ?: error("Series not found")

        val metadataFile =
            File(
                media.series.metadataFolder,
                "metadata.json"
            )

        val metadata =
            metadataStorageRepository.loadMetadata(
                media.series.id,
                SeriesMetadata.serializer()
            )

        Log.d(
            "Anidex_InfoRepo",
            "Loaded Series ${media.series.title}| ${metadata?.plot}"
        )
        Log.d(
            "Anidex_InfoRepo",
            media.series.metadataFolder
        )

        if (metadata==null)
        {
            Log.d(
                "Anidex_Infopage",
                "no Title in ${media.series.title} "
            )
        }

        return SeriesInfoUiModel(

            media = media,

            metadata = metadata,

            playback = media.lastPlayedEpisode?.let {

                dao.getEpisodePlayback(it.id)

            },

            recommendations = emptyList(),

            similar = emptyList()
        )
    }

    suspend fun loadMovie(
        movieId: Long
    ): MovieInfoUiModel {

        val movie =
            movieRepository.getMovieById(movieId)
                ?: error("Movie not found")

        val metadataFile =
            File(
                movie.movie.metadataFolder,
                "metadata.json"
            )

        val metadata =
            metadataStorageRepository.loadMovieMetadata(
                movie.movie.id,
                MovieMetadata.serializer()
            )



        return MovieInfoUiModel(

            movie = movie,

            metadata = metadata,

            playback = movie.playback
        )
    }
}