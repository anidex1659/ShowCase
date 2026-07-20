package com.example.showcase.ui.popup.Dialogs.Repository


import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

class ArtworkRepository {

    fun getArtworkFiles(
        context: Context,
        seriesFolderUri: String
    ): List<Pair<String, String?>> {

        val folder =
            DocumentFile.fromTreeUri(
                context,
                Uri.parse(seriesFolderUri)
            ) ?: return emptyList()

        val showcase =
            folder.findFile(".showcase")
                ?: return emptyList()

        fun find(
            fileName: String
        ): String? {

            return showcase
                .findFile(fileName)
                ?.uri
                ?.toString()
        }

        return listOf(

            "Poster" to
                    find("poster.jpg"),

            "Backdrop" to
                    find("backdrop.jpg"),

            "Banner" to
                    find("banner.jpg"),

            "Thumb" to
                    find("thumb.jpg"),

            "Logo" to
                    find("logo.png")
        )
    }
}