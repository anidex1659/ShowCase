package com.example.showcase.features.MetaData.data.scanner.Detectors

import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.scanner.ScanResult

class SeriesDetector {

    private val stopWords = setOf(

        "1080P",
        "720P",
        "2160P",
        "480P",

        "WEB-DL",
        "WEBRIP",
        "BLURAY",
        "HDRIP",

        "X264",
        "X265",
        "H264",
        "H265",
        "HEVC",

        "AAC",
        "NF",
        "DUAL",

        "HINDI",
        "ENGLISH",
        "KOREAN",
        "JAPANESE",

        "MSUBS",
        "ESUB",

        "VEGAMOVIES"
    )

    fun detect(
        mediaFile: MediaFile
    ): ScanResult {

        val cleanedName = cleanFileName(mediaFile.name) // removes . _ __

        val withoutJunk = removeJunk(cleanedName)

        val seriesName =
            mediaFile.parentFolderName
                ?.takeIf { it.isNotBlank() }
                ?: extractShowName(withoutJunk)

        return ScanResult(

			seriesTitle = seriesName,

			seasonNumber =
				extractSeason(withoutJunk),

			episodeNumber =
				extractEpisode(withoutJunk),

			filePath =
				mediaFile.uri.toString(),

			folderPath =
				mediaFile.parentFolder ?: ""
		)
    }

    private fun cleanFileName(
        fileName: String
    ): String {

        return fileName
            .substringBeforeLast(".")
            .replace(".", " ")
            .replace("_", " ")
            .replace("-", " ")
            .trim()
    }

    private fun extractSeason(
        fileName: String
    ): Int? {

        val match =
            Regex(
                "S(\\d{1,2})E\\d{1,3}",
                RegexOption.IGNORE_CASE
            ).find(fileName)

        return match
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull()
    }

    private fun extractEpisode(
        fileName: String
    ): Int? {

        val match =
            Regex(
                "S\\d{1,2}E(\\d{1,3})",
                RegexOption.IGNORE_CASE
            ).find(fileName)

        return match
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull()
    }

    private fun extractShowName(
        fileName: String
    ): String {

        return fileName
            .replace(

                Regex(
                    "S\\d{1,2}E\\d{1,3}.*",
                    RegexOption.IGNORE_CASE
                ),

                ""
            )
            .trim()
    }

    private fun removeJunk(
        text: String
    ): String {

        val words =
            text.split(" ")

        val result =
            mutableListOf<String>()

        for (word in words) {

            if (
                stopWords.contains(
                    word.uppercase()
                )
            ) {
                break
            }

            result.add(word)
        }

        return result.joinToString(" ")
    }

}