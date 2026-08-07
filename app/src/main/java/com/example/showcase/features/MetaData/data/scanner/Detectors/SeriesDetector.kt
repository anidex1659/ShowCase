package com.example.showcase.features.MetaData.data.scanner.Detectors

import android.util.Log
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


        Log.d("SeriesDetector", "Original : ${mediaFile.name}")
        Log.d("SeriesDetector", "Cleaned  : $cleanedName")
        Log.d("SeriesDetector", "No Junk  : $withoutJunk")
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

    private val seasonEpisodePatterns = listOf(

        // S01E01
        Regex("S(\\d{1,2})\\s*E(\\d{1,3})", RegexOption.IGNORE_CASE),

        // Season ,1 Episode 1
        Regex(
            "Season\\s*(\\d{1,2})\\s*Episode\\s*(\\d{1,3})",
            RegexOption.IGNORE_CASE
        ),

        // 1x01
        Regex("(\\d{1,2})x(\\d{1,3})", RegexOption.IGNORE_CASE)
    )

    private val episodeOnlyPatterns = listOf(

        Regex("Episode\\s*(\\d{1,3})", RegexOption.IGNORE_CASE),

        Regex("Ep\\s*(\\d{1,3})", RegexOption.IGNORE_CASE),

        Regex("E(\\d{1,3})\\b", RegexOption.IGNORE_CASE)
    )

    private fun extractSeason(fileName: String): Int? {

        seasonEpisodePatterns.forEach { regex ->

            regex.find(fileName)?.let {
                return it.groupValues[1].toIntOrNull()
            }
        }

        Regex(
            "Season\\s*(\\d{1,2})",
            RegexOption.IGNORE_CASE
        ).find(fileName)?.let {

            return it.groupValues[1].toIntOrNull()
        }

        return null
    }

    private fun extractEpisode(fileName: String): Int? {

        seasonEpisodePatterns.forEach { regex ->

            regex.find(fileName)?.let {
                return it.groupValues[2].toIntOrNull()
            }
        }

        episodeOnlyPatterns.forEach { regex ->

            regex.find(fileName)?.let {
                return it.groupValues[1].toIntOrNull()
            }
        }

        return null
    }

    private fun extractShowName(fileName: String): String {

        var title = fileName

        val patterns = listOf(

            Regex(
                "S\\d{1,2}\\s*E\\d{1,3}.*",
                RegexOption.IGNORE_CASE
            ),

            Regex(
                "Season\\s*\\d{1,2}\\s*Episode\\s*\\d{1,3}.*",
                RegexOption.IGNORE_CASE
            ),

            Regex(
                "\\d{1,2}x\\d{1,3}.*",
                RegexOption.IGNORE_CASE
            ),

            Regex(
                "Episode\\s*\\d{1,3}.*",
                RegexOption.IGNORE_CASE
            ),

            Regex(
                "Ep\\s*\\d{1,3}.*",
                RegexOption.IGNORE_CASE
            ),

            Regex(
                "E\\d{1,3}.*",
                RegexOption.IGNORE_CASE
            )
        )

        patterns.forEach {
            title = title.replace(it, "")
        }

        return title.trim()
    }

    private fun removeJunk(
        text: String
    ): String {

        return text
            .replace(Regex("\\b(480P|720P|1080P|2160P)\\b", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\b(WEB[- ]?DL|WEBRIP|BLURAY|HDRIP)\\b", RegexOption.IGNORE_CASE), "")
            .replace(
                Regex(
                    "\\b(X264|X265|H264|H265|HEVC|AAC\\d*(\\.\\d+)?)\\b",
                    RegexOption.IGNORE_CASE
                ), ""
            )
            .replace(Regex("\\b(VEGAMOVIES|TO|IS|TW)\\b", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\s+"), " ")
            .trim()

    }
}