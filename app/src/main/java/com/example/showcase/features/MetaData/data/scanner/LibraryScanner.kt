package com.example.showcase.features.MetaData.data.scanner

import android.util.Log
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.series.EpisodeEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity
import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.model.SeriesMetadata
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.MetaData.data.repository.metadata.sires.MetadataRepository
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.Player.model.storage.StorageLocationManager


class LibraryScanner(

    private val detector: SeriesDetector,
    private val metadataRepository: MetadataRepository,
    private val metadataStorageRepository: MetadataStorageRepository,
    private val dao: ShowcaseDao,
    private val artworkDownloader: ArtworkDownloader,
    private val remote: TmdbRemoteDataSource

) {

    suspend fun scan(
        libraryId: Long, mediaFiles: List<MediaFile>
    ) {
        mediaFiles.forEach {
            processFile(it, libraryId)
        }
    }

    private suspend fun processFile(
        mediaFile: MediaFile, libraryId: Long
    ) {

        val result = detector.detect(mediaFile)

        val metadataFolderPath = "${result.folderPath}/.showcase"
        var series = dao.getSeriesByTitle(
            result.seriesTitle
        )




        if (series == null) {


            val id = dao.insertSeries(

                SeriesEntity(

                    title = result.seriesTitle,
                    libraryId = libraryId,
                    folderPath = result.folderPath,
                    metadataFolder = metadataFolderPath,
                    episodeCount = 0,
                    addedDate = System.currentTimeMillis(),
                    lastScan = System.currentTimeMillis()

                )
            )

            Log.d(
                "Anidex_Scanner",
                "Added Series ${result.seriesTitle} | FolderPath ${result.folderPath}"
            )

            series = dao.getSeriesById(id)

            // Fetch metadata only for newly created series
            series?.let {

                ProgressManager.update(

                    title = "Fetching metadata",

                    subtitle = series.title,

                    current = + 1,

                    total = 0
                )

                val fetchResult =
                    metadataRepository
                        .fetchSeriesMetadata(
                            it.title
                        )

                if (fetchResult != null) {

                    dao.updateTmdbId(
                        seriesId = it.id,
                        tmdbId = fetchResult.tvId //store Starting Tmdb ID
                    )

                    val storageLocation =

                        StorageLocationManager
                            .seriesStorageFolder(
                                it.folderPath
                            )

                    metadataStorageRepository.saveMetadata(

                        mediaId = it.id,

                        storageLocation = storageLocation,

                        metadata = fetchResult.metadata,

                        serializer = SeriesMetadata.serializer()

                    )

//                    val storageLocation =
//
//                        StorageLocationManager
//                            .seriesStorageFolder(
//                                series.folderPath
//                            )
                    ProgressManager.update(

                        title = "Downloading artwork",

                        subtitle = series.title,

                        current = + 1,

                        total = 0
                    )

                    artworkDownloader.downloadSeriesArtwork(

                        seriesId = series.id,

                        storageLocation = storageLocation,

                        posterUrl = remote.buildImageUrl(fetchResult.posterPath),

                        backdropUrl = remote.buildImageUrl(fetchResult.backdropPath),

                        bannerUrl = remote.buildImageUrl(fetchResult.backdropPath),

                        thumbUrl = remote.buildImageUrl(fetchResult.posterPath),

                        logoUrl = remote.buildImageUrl(fetchResult.logoPath)

                    )

                    Log.d(
                        "Anidex_Artwork",
                        "Artwork downloaded for seriesId : ${it.id} | ${it.title}  | TmDb ID : ${fetchResult.tvId}"
                    )
                }
            }



        }


        val currentSeries = series ?: return

        val storageLocation =
            StorageLocationManager.seriesStorageFolder(
                currentSeries.folderPath
            )

        val metadata =
            metadataStorageRepository.loadMetadata(
                mediaId = series.id,

                serializer = SeriesMetadata.serializer()

            )

        val existingEpisode = dao.getEpisode(  //check if episode exists

            seriesId = currentSeries.id,
            seasonNumber = result.seasonNumber ?: 1,
            episodeNumber = result.episodeNumber ?: 0

        )

        if (existingEpisode == null) {

            val episodeTitle =

                metadata?.seasons
                    ?.firstOrNull {
                        it.seasonNumber ==
                                (result.seasonNumber ?: 1)
                    }
                    ?.episodes
                    ?.firstOrNull {
                        it.episodeNumber ==
                                (result.episodeNumber ?: 0)
                    }
                    ?.title
                    ?: "Episode ${result.episodeNumber}"

            // add episode
            dao.insertEpisode(

                EpisodeEntity(

                    seriesId = currentSeries.id,
                    seasonNumber = result.seasonNumber ?: 1,
                    episodeNumber = result.episodeNumber ?: 0,
                    title = episodeTitle,
                    filePath = result.filePath,
                    duration = 0,
                    watched = false,
                    progress = 0,
                    lastPlayed = 0

                )
            )

            Log.d(
                "Anidex_Scanner",
                "Added S${result.seasonNumber} | Ep${result.episodeNumber} | $episodeTitle"
            )

            val episodeCount = dao.getEpisodeCount(
                currentSeries.id
            )

            dao.updateEpisodeCount(

                seriesId = currentSeries.id,
                count = episodeCount,
                lastScan = System.currentTimeMillis()

            )

        }
    }
}