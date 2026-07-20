package com.example.showcase.ui.popup.Dialogs.Repository

import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.model.SeriesMetadata
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.MetaData.data.repository.metadata.sires.MetadataRepository
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.Player.model.storage.StorageLocationManager

class IdentifySeriesRepository(

    private val dao: ShowcaseDao,

    private val metadataRepository: MetadataRepository,

    private val metadataStorageRepository: MetadataStorageRepository,

    private val artworkDownloader: ArtworkDownloader,

    private val remote: TmdbRemoteDataSource

) {

    suspend fun identifySeries(

        seriesId: Long,

        tmdbId: Int

    ): Boolean {

        val series =
            dao.getSeriesById(seriesId)
                ?: return false

        dao.updateTmdbId(
            seriesId,
            tmdbId
        )

        val result =
            metadataRepository.fetchMetadataByTmdbId(
                tmdbId
            ) ?: return false

        val storageLocation =
            StorageLocationManager.seriesStorageFolder(
                series.folderPath
            )

        ProgressManager.show("Updating Metadata",series.title)

        metadataStorageRepository.saveMetadata(

            mediaId = seriesId,

            storageLocation = storageLocation,

            metadata = result.metadata,

            serializer = SeriesMetadata.serializer()

        )


        ProgressManager.show("Updating Artworks",series.title)

        artworkDownloader.downloadSeriesArtwork(

            seriesId = series.id,

            storageLocation = storageLocation,

            posterUrl = remote.buildImageUrl(
                result.posterPath
            ),

            backdropUrl = remote.buildImageUrl(
                result.backdropPath
            ),

            bannerUrl = remote.buildImageUrl(
                result.backdropPath
            ),

            thumbUrl = remote.buildImageUrl(
                result.posterPath
            ),

            logoUrl = remote.buildImageUrl(
                result.logoPath
            )

        )
        ProgressManager.hide()

        return true
    }
}