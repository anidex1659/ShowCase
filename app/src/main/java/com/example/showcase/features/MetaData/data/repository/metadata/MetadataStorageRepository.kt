package com.example.showcase.features.MetaData.data.repository.metadata

import android.content.Context
import android.util.Log
import com.example.showcase.features.Player.model.storage.MetadataJsonManager
import com.example.showcase.features.Player.model.storage.MovieMetadataJsonManager
import kotlinx.serialization.KSerializer

class MetadataStorageRepository(

    private val context: Context,

    private val jsonManager: MetadataJsonManager,

    private val movieJsonManager: MovieMetadataJsonManager

) {

    suspend fun <T> saveMetadata(

        mediaId: Long,

        storageLocation: String,

        metadata: T,

        serializer: KSerializer<T>

    ): Boolean {

        if (

            jsonManager.loadMetadata(

                context = context,

                seriesId = mediaId,

                serializer = serializer

            ) != null

        ) {

            Log.d(
                "Anidex_Metadata",
                "Updating metadata"
            )

        } else {

            Log.d(
                "Anidex_Metadata",
                "Creating metadata"
            )

        }

        return jsonManager.saveMetadata(

            context = context,

            mediaId = mediaId,

            metadata = metadata,

            serializer = serializer,

            storageFolderUri = storageLocation

        )
    }

    suspend fun <T> loadMetadata(

        mediaId: Long,

        serializer: KSerializer<T>

    ): T? {

        return jsonManager.loadMetadata(

            context = context,

            seriesId = mediaId,

            serializer = serializer

        )
    }

    suspend fun <T> saveMovieMetadata(

        movieId: Long,

        storageLocation: String,

        metadata: T,

        serializer: KSerializer<T>

    ): Boolean {

        if (

            movieJsonManager.loadMetadata(

                context = context,

                seriesId = movieId,

                serializer = serializer

            ) != null

        ) {

            Log.d(
                "Movie_Metadata",
                "Updating metadata"
            )

        } else {

            Log.d(
                "Movie_Metadata",
                "Creating metadata"
            )

        }

        return movieJsonManager.saveMetadata(

            context = context,

            movieId = movieId,

            metadata = metadata,

            serializer = serializer,

            storageFolderUri = storageLocation

        )
    }

    suspend fun <T> loadMovieMetadata(

        movieId: Long,

        serializer: KSerializer<T>

    ): T? {

        return movieJsonManager.loadMetadata(

            context = context,

            seriesId = movieId,

            serializer = serializer

        )
    }
}