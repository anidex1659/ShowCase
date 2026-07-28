package com.example.showcase.ui.popup.Dialogs.Repository


import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

class ArtworkRepository {


    fun getArtworkFiles(
        context: Context,
        folderUri: String
    ): List<Pair<String, String?>> {

        val folder =
            DocumentFile.fromTreeUri(
                context,
                Uri.parse(folderUri)
            ) ?: return emptyList()

        val showcaseFolder =
            folder.findFile(".showcase")
                ?: return emptyList()

        fun find(vararg names: String): String? {

            names.forEach { name ->

                val file =
                    showcaseFolder.findFile(name)

                if (file != null) {
                    return file.uri.toString()
                }
            }

            return null
        }

        return listOf(

            "Poster" to
                    find(
                        "poster.jpg",
                        "poster.jpeg",
                        "poster.png"
                    ),

            "Backdrop" to
                    find(
                        "backdrop.jpg",
                        "backdrop.jpeg",
                        "backdrop.png"
                    ),

            "Banner" to
                    find(
                        "banner.jpg",
                        "banner.jpeg",
                        "banner.png"
                    ),

            "Thumb" to
                    find(
                        "thumb.jpg",
                        "thumb.jpeg",
                        "thumb.png"
                    ),

            "Logo" to
                    find(
                        "logo.png",
                        "logo.webp",
                        "logo.jpg"
                    )
        )
    }

    fun getArtwork(
        context: Context,
        folderUri: String,
        type: String
    ): String? {

        val folder =
            DocumentFile.fromTreeUri(
                context,
                Uri.parse(folderUri)
            ) ?: return null

        val showcaseFolder =
            folder.findFile(".showcase")
                ?: return null

        val names =
            when (type.lowercase()) {

                "poster" ->
                    listOf(
                        "poster.jpg",
                        "poster.jpeg",
                        "poster.png"
                    )

                "backdrop" ->
                    listOf(
                        "backdrop.jpg",
                        "backdrop.jpeg",
                        "backdrop.png"
                    )

                "banner" ->
                    listOf(
                        "banner.jpg",
                        "banner.jpeg",
                        "banner.png"
                    )

                "thumb" ->
                    listOf(
                        "thumb.jpg",
                        "thumb.jpeg",
                        "thumb.png"
                    )

                "logo" ->
                    listOf(
                        "logo.png",
                        "logo.webp",
                        "logo.jpg"
                    )

                else ->
                    return null
            }

        names.forEach { name ->

            showcaseFolder
                .findFile(name)
                ?.let {
                    return it.uri.toString()
                }
        }

        return null
    }
}