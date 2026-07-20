package com.example.showcase.features.Player.model.storage

import android.content.Context
import android.util.Log
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class MetadataJsonManager(

    private val dao: ShowcaseDao

) {

    private val json = Json {

        prettyPrint = true

        ignoreUnknownKeys = true
    }

    suspend fun <T> saveMetadata(

        context: Context,

        mediaId: Long,

        metadata: T,

        serializer: KSerializer<T>,

        storageFolderUri: String

    ): Boolean {

        return try {

            val folder =

                StorageLocationManager.ensureFolder(

                    context,

                    storageFolderUri

                ) ?: return false

            folder.findFile("metadata.json")
                ?.delete()

            val metadataFile =

                folder.createFile(

                    "application/json",

                    "metadata"

                ) ?: return false

            val jsonText =

                json.encodeToString(

                    serializer,

                    metadata

                )

            context.contentResolver
                .openOutputStream(
                    metadataFile.uri,
                    "wt"
                )
                ?.use {

                    it.write(
                        jsonText.toByteArray()
                    )

                }

            dao.deleteArtwork(

                mediaId,

                "metadata"

            )

            dao.insertArtwork(

                ArtworkEntity(

                    seriesId = mediaId,

                    type = "metadata",

                    fileName = "metadata.json",

                    available = true,

                    uri = metadataFile.uri.toString(),

                    source = "ShowCase"

                )

            )

            true

        } catch (e: Exception) {

            Log.e(

                "Anidex_Metadata",

                "Failed saving metadata",

                e

            )

            false
        }
    }

    suspend  fun <T> loadMetadata(
        context: Context,
        seriesId: Long,
        serializer: KSerializer<T>
    ): T? {

        return try {

            val artwork = dao.getArtwork(
                seriesId,
                "metadata"
            ) ?: return null

            Log.d(
                "Anidex_MetadataLoad",
                "Metadata URI = ${artwork.uri}"
            )

            val jsonText =
                context.contentResolver
                    .openInputStream(android.net.Uri.parse(artwork.uri))
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: return null

            Log.d(
                "Anidex_MetadataLoad",
                jsonText
            )

            json.decodeFromString(
                serializer,
                jsonText
            )

        } catch (e: Exception) {

            Log.e(
                "Anidex_MetadataLoad",
                "Failed loading metadata",
                e
            )

            null
        }
    }
}