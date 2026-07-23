package com.example.showcase.features.MetaData.data.repository.Artwork

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import com.example.showcase.features.Player.model.storage.ArtworkStorageManager
import com.google.common.collect.Multimaps.index

class ArtworkDownloader(

    private val context: Context,

    private val storageManager: ArtworkStorageManager,

    private val dao: ShowcaseDao

) {

    private val imageLoader = ImageLoader(context)

    private suspend fun downloadImage(

        mediaId: Long,

        artworkType: String,

        imageUrl: String?,

        storageLocation: String,

        fileName: String,

        mimeType: String

    ): Boolean {

        if (imageUrl.isNullOrBlank())
            return false

        return try {

            val result = imageLoader.execute(

                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build()

            )

            if (result !is SuccessResult)
                return false

            val file = storageManager.getArtworkFile(

                context = context,

                storageFolderUri = storageLocation,

                fileName = fileName,

                mimeType = mimeType

            ) ?: return false

            context.contentResolver
                .openOutputStream(file.uri, "wt")
                ?.use {

                    val format =
                        if (fileName.endsWith(".png", true))
                            Bitmap.CompressFormat.PNG
                        else
                            Bitmap.CompressFormat.JPEG

                    result.drawable
                        .toBitmap()
                        .compress(
                            format,
                            95,
                            it
                        )
                }

            dao.deleteArtwork(
                mediaId,
                artworkType
            )

            dao.insertArtwork(

                ArtworkEntity(

                    // Rename later to mediaId if desired
                    seriesId = mediaId,

                    type = artworkType,

                    fileName = fileName,

                    available = true,

                    uri = file.uri.toString(),

                    source = "TMDB"

                )

            )

            Log.d(
                "Anidex_Artwork",
                "$artworkType downloaded"
            )

            true

        } catch (e: Exception) {

            Log.e(
                "Anidex_Artwork",
                "Failed downloading $artworkType",
                e
            )

            false
        }
    }

    suspend fun replaceSeriesArtwork(
        seriesId: Long,
        artworkType: String,
        imageUrl: String,
        storageLocation: String
    ): Boolean {

        val fileName =
            when (artworkType) {

                "poster" ->
                    "poster.jpg"

                "backdrop" ->
                    "backdrop.jpg"

                "banner" ->
                    "banner.jpg"

                "thumb" ->
                    "thumb.jpg"

                "logo" ->
                    "logo.png"

                else ->
                    return false
            }

        val mimeType =
            if (artworkType == "logo")
                "image/png"
            else
                "image/jpeg"

        return downloadImage(

            mediaId = seriesId,

            artworkType = artworkType,

            imageUrl = imageUrl,

            storageLocation = storageLocation,

            fileName = fileName,

            mimeType = mimeType

        )
    }

    suspend fun replaceMovieArtwork(
        movieId: Long,
        artworkType: String,
        imageUrl: String,
        storageLocation: String
    ): Boolean {

        val fileName =
            when (artworkType) {

                "poster" ->
                    "poster.jpg"

                "backdrop" ->
                    "backdrop.jpg"

                "logo" ->
                    "logo.png"

                else ->
                    return false
            }

        val mimeType =
            if (artworkType == "logo")
                "image/png"
            else
                "image/jpeg"

        return downloadMovieImage(
            movieId = movieId,
            artworkType = artworkType,
            imageUrl = imageUrl,
            storageLocation = storageLocation,
            fileName = fileName,
            mimeType = mimeType
        )
    }

    suspend fun downloadPoster(

        mediaId: Long,

        url: String?,

        storageLocation: String

    ) = downloadImage(

        mediaId,

        artworkType = "poster",

        imageUrl = url,

        storageLocation = storageLocation,

        fileName = "poster.jpg",

        mimeType = "image/jpeg"

    )

    suspend fun downloadBackdrop(

        mediaId: Long,

        url: String?,

        storageLocation: String

    ) = downloadImage(

        mediaId,

        artworkType = "backdrop",

        imageUrl = url,

        storageLocation = storageLocation,

        fileName = "backdrop.jpg",

        mimeType = "image/jpeg"

    )

    suspend fun downloadBanner(

        mediaId: Long,

        url: String?,

        storageLocation: String

    ) = downloadImage(

        mediaId,

        artworkType = "banner",

        imageUrl = url,

        storageLocation = storageLocation,

        fileName = "banner.jpg",

        mimeType = "image/jpeg"

    )

    suspend fun downloadThumb(

        mediaId: Long,

        url: String?,

        storageLocation: String

    ) = downloadImage(

        mediaId,

        artworkType = "thumb",

        imageUrl = url,

        storageLocation = storageLocation,

        fileName = "thumb.jpg",

        mimeType = "image/jpeg"

    )

    suspend fun downloadLogo(

        mediaId: Long,

        url: String?,

        storageLocation: String

    ) = downloadImage(

        mediaId,

        artworkType = "logo",

        imageUrl = url,

        storageLocation = storageLocation,

        fileName = "logo.png",

        mimeType = "image/png"

    )

    /**
     * Generic artwork downloader.
     * Works for BOTH Series and Movies.
     */
    suspend fun downloadAllArtwork(

        mediaId: Long,

        storageLocation: String,

        posterUrl: String?,

        backdropUrl: String?,

        bannerUrl: String? = null,

        thumbUrl: String? = null,

        logoUrl: String? = null

    ) {

        downloadPoster(
            mediaId,
            posterUrl,
            storageLocation
        )

        downloadBackdrop(
            mediaId,
            backdropUrl,
            storageLocation
        )

        if (bannerUrl != null)
            downloadBanner(
                mediaId,
                bannerUrl,
                storageLocation
            )

        if (thumbUrl != null)
            downloadThumb(
                mediaId,
                thumbUrl,
                storageLocation
            )

        if (logoUrl != null)
            downloadLogo(
                mediaId,
                logoUrl,
                storageLocation
            )
    }
    suspend fun downloadSeriesArtwork(

        seriesId: Long,

        storageLocation: String,

        posterUrl: String?,

        backdropUrl: String?,

        bannerUrl: String?,

        thumbUrl: String?,

        logoUrl: String?

    ) {

        downloadAllArtwork(

            mediaId = seriesId,

            storageLocation = storageLocation,

            posterUrl = posterUrl,

            backdropUrl = backdropUrl,

            bannerUrl = bannerUrl,

            thumbUrl = thumbUrl,

            logoUrl = logoUrl

        )
    }

    //==============================================================================================
    //MOVIES========================================================================================

    suspend fun downloadMovieArtwork(

        movieId: Long,

        storageLocation: String,

        posterUrl: String?,

        backdropUrl: String?,

        logoUrl: String?

    ) {

        downloadMoviePoster(

            movieId,

            posterUrl,

            storageLocation

        )

        downloadMovieBackdrop(

            movieId,

            backdropUrl,

            storageLocation

        )

        downloadMovieLogo(

            movieId,

            logoUrl,

            storageLocation

        )
    }


    private suspend fun downloadMovieImage(

        movieId: Long,

        artworkType: String,

        imageUrl: String?,

        storageLocation: String,

        fileName: String,

        mimeType: String

    ): Boolean {

        if (imageUrl.isNullOrBlank())
            return false

        return try {

            val result = imageLoader.execute(

                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build()

            )

            if (result !is SuccessResult)
                return false

            val file = storageManager.getArtworkFile(

                context = context,

                storageFolderUri = storageLocation,

                fileName = fileName,

                mimeType = mimeType

            ) ?: return false

            context.contentResolver
                .openOutputStream(file.uri, "wt")
                ?.use {

                    val format =
                        if (fileName.endsWith(".png", true))
                            Bitmap.CompressFormat.PNG
                        else
                            Bitmap.CompressFormat.JPEG

                    result.drawable
                        .toBitmap()
                        .compress(
                            format,
                            95,
                            it
                        )
                }

            dao.deleteMovieArtwork(
                movieId,
                artworkType
            )

            dao.insertMovieArtwork(

                MovieArtworkEntity(

                    movieId = movieId,

                    type = artworkType,

                    fileName = fileName,

                    available = true,

                    uri = file.uri.toString(),

                    source = "TMDB"

                )

            )

            ProgressManager.hide()

            Log.d(
                "MovieArtwork",
                "$artworkType downloaded"
            )

            true

        } catch (e: Exception) {

            Log.e(
                "MovieArtwork",
                "Download failed",
                e
            )

            false
        }
    }

    private suspend fun downloadMoviePoster(

        movieId: Long,

        url: String?,

        storageLocation: String

    ) = downloadMovieImage(

        movieId,

        "poster",

        url,

        storageLocation,

        "poster.jpg",

        "image/jpeg"

    )

    private suspend fun downloadMovieBackdrop(

        movieId: Long,

        url: String?,

        storageLocation: String

    ) = downloadMovieImage(

        movieId,

        "backdrop",

        url,

        storageLocation,

        "backdrop.jpg",

        "image/jpeg"

    )

    private suspend fun downloadMovieLogo(

        movieId: Long,

        url: String?,

        storageLocation: String

    ) = downloadMovieImage(

        movieId,

        "logo",

        url,

        storageLocation,

        "logo.png",

        "image/png"

    )



}