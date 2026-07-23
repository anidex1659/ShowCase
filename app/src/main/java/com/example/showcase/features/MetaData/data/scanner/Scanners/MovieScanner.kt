package com.example.showcase.features.MetaData.data.scanner.Scanners

import android.util.Log
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieEntity
import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.model.MovieMetadata
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.MetaData.data.repository.metadata.sires.MetadataRepository
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.MetaData.data.scanner.Detectors.MovieDetector
import com.example.showcase.features.Player.model.storage.StorageLocationManager

class MovieScanner(

    private val detector: MovieDetector,

    private val dao: ShowcaseDao,

    private val metadataRepository: MetadataRepository,

    private val metadataStorageRepository: MetadataStorageRepository,

    private val artworkDownloader: ArtworkDownloader,

    private val remote: TmdbRemoteDataSource

) {

    suspend fun scan(

        libraryId: Long,

        mediaFiles: List<MediaFile>

    ) {

        mediaFiles.forEach {

            processMovie(
                libraryId,
                it
            )
        }
    }

    private suspend fun processMovie(

        libraryId: Long,

        mediaFile: MediaFile

    ) {

        val result =
            detector.detect(mediaFile)

        var movie =
            dao.getMovieByTitle(result.title)

        Log.d("Anidex_MovieScanner", "Inserted movie = $movie")

        if (movie == null) {

            val id = dao.insertMovie(

                MovieEntity(

                    libraryId = libraryId,

                    title = result.title,

                    folderPath = result.folderPath,

                    metadataFolder = "",

                    filePath = result.filePath,

                    addedDate = System.currentTimeMillis(),

                    lastScan = System.currentTimeMillis()

                )

            )

            movie = dao.getMovieById(id)

        }

        movie ?: return

        if (movie.tmdbId != null)
            return

        Log.d(
            "Anidex_MovieScanner",
            "Fetching metadata for ${movie.title}"
        )

        ProgressManager.update(

            title = "Fetching metadata",

            subtitle = movie.title,

            current =+ 1,

            total = 0
        )

        val fetchResult =
            metadataRepository.fetchMovieMetadata(
                movie.title
            ) ?: return


        dao.updateMovieTmdbId(

            movie.id,

            fetchResult.movieId

        )

        val library =

            dao.getLibrary(
                libraryId
            ) ?: return

        val storageLocation =

            StorageLocationManager.movieStorageFolder(

                libraryFolder = library.folderUri,

                movieTitle = movie.title

            )

        metadataStorageRepository.saveMovieMetadata(

            movieId = movie.id,

            storageLocation = storageLocation,

            metadata = fetchResult.metadata,

            serializer = MovieMetadata.serializer()

        )

        ProgressManager.update(

            title = "Downloading artwork",

            subtitle = movie.title,

            current = + 1,

            total = 0
        )

        Log.d("Anidex_MovieScanner", "Downloading artwork for movieId=${movie.id} | Movie exists = ${
            dao.getMovieById(
                movie.id
            ) != null
        }")




        artworkDownloader.downloadMovieArtwork(

            movieId = movie.id,

            storageLocation = storageLocation,

            posterUrl = remote.buildImageUrl(fetchResult.posterPath),

            backdropUrl = remote.buildImageUrl(fetchResult.backdropPath),

            logoUrl = remote.buildImageUrl(fetchResult.logoPath)

        )

        Log.d(

            "Anidex_MovieScanner",

            "Finished ${movie.title}"

        )
        ProgressManager.hide()

    }
}