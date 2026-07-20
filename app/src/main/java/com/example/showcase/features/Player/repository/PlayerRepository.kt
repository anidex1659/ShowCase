package com.example.showcase.features.Player.repository

import android.content.Context
import android.util.Log
import com.example.showcase.features.InfoPage.uistate.MovieInfoUiModel
import com.example.showcase.features.InfoPage.uistate.SeriesInfoUiModel
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.series.PlaybackHistoryEntity
import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.model.SeriesMetadata
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.features.Player.model.SeriesPlaybackData
import com.example.showcase.features.Player.model.storage.MetadataJsonManager
import com.example.showcase.features.Player.model.storage.MovieMetadataJsonManager

class PlayerRepository(

    private val seriesRepository: SeriesRepository,
    private val movieRepository: MovieRepository,
    val dao: ShowcaseDao,
    val metadataStorageRepository: MetadataStorageRepository

    ) {



    suspend fun getSeriesPlayback(
        seriesId: Long
    ): SeriesPlaybackData? {

        val series =
            seriesRepository.getSeriesById(seriesId)
                ?: return null

        val playlist =
            series.seasons
                .sortedBy { it.seasonNumber }
                .flatMap { season ->

                    season.episodes
                        .sortedBy { it.episode.episodeNumber }
                        .map {

                            val episode = it.episode
                            val playback=dao.getEpisodePlayback(it.episode.id)

                            PlayerMedia(

                                id = episode.id,

                                type = MediaType.EPISODE,

                                title = episode.title,

                                videoUri = episode.filePath,

                                poster = series.poster?.uri,

                                backdrop = series.backdrop?.uri,

                                seasonNumber = episode.seasonNumber,

                                episodeNumber = episode.episodeNumber,

                                duration = episode.duration,
                                resume = playback?.position ?: 0L
                            )
                        }
                }

        if (playlist.isEmpty())
            return null

        val lastPlayedId =
            dao.getLastPlayedEpisodeForSeries(seriesId)
                ?.mediaId

        val startIndex =
            playlist.indexOfFirst {

                it.id == lastPlayedId

            }.takeIf { it >= 0 } ?: 0

        return SeriesPlaybackData(

            playlist = playlist,

            startIndex = startIndex
        )
    }
    suspend fun getMovie(
        movieId: Long
    ): PlayerMedia? {

        val movie =
            movieRepository.getMovieById(movieId)
                ?: return null
        val playback= dao.getMoviePlayback(movieId)

        return PlayerMedia(

            id = movie.movie.id,

            type = MediaType.MOVIE,

            title = movie.movie.title,

            videoUri = movie.movie.filePath,

            poster =
                movie.artwork.firstOrNull {
                    it.type=="poster"
                }?.uri,

            backdrop =
                movie.artwork.firstOrNull {
                    it.type=="backdrop"
                }?.uri,

            duration = movie.movie.duration,
            resume =playback?.position ?: 0L
        )
    }




    suspend fun savePlayback(
        state: PlayerState
    ) {

        val media = state.media ?: return

        when(media.type){

            MediaType.EPISODE -> {

                val existing =
                    seriesRepository.dao
                        .getEpisodePlayback(media.id)


                val completed =
                    state.duration > 0 &&
                            state.currentPosition >= state.duration * 0.95


                if (completed) {

                    dao.deleteEpisodePlayback(media.id)

                } else {

                    val entity = PlaybackHistoryEntity(

                        id = existing?.id ?: 0,

                        mediaId = media.id,

                        mediaType = "episode",

                        position = state.currentPosition,

                        duration = state.duration,

                        lastPlayed = System.currentTimeMillis()
                    )

                    if (existing == null)
                        dao.insertPlayback(entity)
                    else
                        dao.updatePlayback(entity)

                }
            }

            MediaType.MOVIE -> {

                val existing =
                    dao.getMoviePlayback(media.id)

                val entity = MoviePlaybackEntity(

                    id = existing?.id ?: 0,

                    movieId = media.id,

                    position = state.currentPosition,

                    duration = state.duration,

                    lastPlayed = System.currentTimeMillis()
                )

                if(existing == null)
                    dao.insertMoviePlayback(entity)
                else
                    dao.updateMoviePlayback(entity)
            }
        }
    }



    suspend fun loadSeriesInfo(
        seriesId: Long
    ): SeriesInfoUiModel {

        val media =
            seriesRepository.getSeriesById(seriesId)
                ?: error("Series not found")

        val metadata =
            metadataStorageRepository.loadMetadata(
                media.series.id,
                SeriesMetadata.serializer()
            )

        Log.d(
            "Anidex_PlayerRepo",
            "movie = $media"
        )

        Log.d(
            "Anidex_PlayerRepo",
            "metadata = $metadata"
        )

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

    suspend fun loadMovieInfo(
        movieId: Long
    ): MovieInfoUiModel {

        val movie =
            movieRepository.getMovieById(movieId)
                ?: error("Movie not found")

        val metadata =
            metadataStorageRepository.loadMovieMetadata(
                movie.movie.id,
                MovieMetadata.serializer()
            )

        Log.d(
            "Anidex_PlayerRepo",
            "movie = $movie"
        )

        Log.d(
            "Anidex_PlayerRepo",
            "metadata = $metadata"
        )

        return MovieInfoUiModel(

            movie = movie,

            metadata = metadata,

            playback = movie.playback
        )
    }
}
