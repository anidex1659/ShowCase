package com.example.showcase.features.MetaData.data.repository.metadata.sires

import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity
import com.example.showcase.features.MetaData.data.model.EpisodeUiModel
import com.example.showcase.features.MetaData.data.model.SeasonUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel

class SeriesRepository(

    val dao: ShowcaseDao

) {

    suspend fun getSeries(): List<SeriesUiModel> {

        return dao
            .getAllSeries()
            .map {
                buildSeriesUiModel(it)
            }
    }

    suspend fun getSeriesById(
        id: Long
    ): SeriesUiModel? {

        val series =
            dao.getSeriesById(id)
                ?: return null

        return buildSeriesUiModel(series)
    }

    suspend fun getSeriesByName(
        name: String
    ): SeriesUiModel? {

        val series =
            dao.getSeriesByTitle(name)
                ?: return null

        return buildSeriesUiModel(series)
    }

    suspend fun getSeriesByLibrary(
        libraryId: Long
    ): List<SeriesUiModel> {

        return dao
            .getSeriesForLibrary(libraryId)
            .map {
                buildSeriesUiModel(it)
            }
    }

    suspend fun getRecentlyAdded(
        limit: Int = 20
    ): List<SeriesUiModel> {

        return dao
            .getRecentlyAddedSeries(limit)
            .map {
                buildSeriesUiModel(it)
            }
    }

    private suspend fun buildSeriesUiModel(
        series: SeriesEntity
    ): SeriesUiModel {

        val episodes =
            dao.getEpisodesForSeries(series.id)

        val artwork =
            dao.getArtworkForSeries(series.id)

        val poster =
            artwork.firstOrNull { it.type == "poster" }

        val logo =
            artwork.firstOrNull { it.type == "logo" }

        val backdrop =
            artwork.firstOrNull { it.type == "backdrop" }

        val episodeUiModels = episodes.map { episode ->

            val playback =
                dao.getEpisodePlayback(episode.id)

            val watched =
                playback?.let {
                    it.duration > 0 &&
                            it.position >= (it.duration * 0.95f)
                } ?: false

            val progress =
                playback?.let {
                    if (it.duration > 0)
                        it.position.toFloat() / it.duration
                    else
                        0f
                } ?: 0f

            EpisodeUiModel(

                episode = episode,

                playback = playback,

                watched = watched,

                progressPercent = progress
            )
        }

        val seasons =
            episodeUiModels
                .groupBy { it.episode.seasonNumber }
                .map { (seasonNumber, episodeList) ->

                    SeasonUiModel(

                        seasonNumber = seasonNumber,

                        episodes = episodeList
                    )
                }
                .sortedBy {
                    it.seasonNumber
                }

        return SeriesUiModel(

            series = series,

            seasons = seasons,

            poster = poster,

            logo = logo,

            backdrop = backdrop,

            episodeCount = episodes.size,

            watchedEpisodes =
                episodeUiModels.count { it.watched },

            lastPlayedEpisode =
                episodeUiModels
                    .filter { it.playback != null }
                    .maxByOrNull {
                        it.playback!!.lastPlayed
                    }
                    ?.episode
        )
    }
}