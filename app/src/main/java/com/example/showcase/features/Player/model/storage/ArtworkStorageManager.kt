package com.example.showcase.features.Player.model.storage

import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile

class ArtworkStorageManager {

    fun getArtworkFile(

        context: Context,

        storageFolderUri: String,

        fileName: String,

        mimeType: String

    ): DocumentFile? {

        return try {

            val folder =
                StorageLocationManager.ensureFolder(
                    context,
                    storageFolderUri
                ) ?: return null

            folder
                .findFile(fileName)
                ?.delete()

            folder.createFile(
                mimeType,
                fileName.substringBeforeLast(".")
            )

        } catch (e: Exception) {

            Log.e(
                "Anidex_ArtworkStorage",
                "Failed creating artwork file",
                e
            )

            null
        }
    }
}