package com.example.showcase.features.Player.model.storage

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

object StorageLocationManager {

    fun seriesStorageFolder(
        seriesFolder: String
    ): String {

        return "$seriesFolder|.showcase"
    }

    fun movieStorageFolder(
        libraryFolder: String,
        movieTitle: String
    ): String {

        return "$libraryFolder|.showcase/$movieTitle"
    }

    fun ensureFolder(
        context: Context,
        storageLocation: String
    ): DocumentFile? {

        val parts = storageLocation.split("|")

        val baseUri = parts[0]

        val relativePath =
            if (parts.size > 1)
                parts[1]
            else
                ""

        var current =
            DocumentFile.fromTreeUri(
                context,
                Uri.parse(baseUri)
            ) ?: return null

        if (relativePath.isBlank())
            return current

        relativePath
            .split("/")
            .filter { it.isNotBlank() }
            .forEach { folderName ->

                var child =
                    current.findFile(folderName)

                if (child == null)
                    child =
                        current.createDirectory(folderName)

                current = child ?: return null
            }

        return current
    }
}