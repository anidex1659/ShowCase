package com.example.showcase.features.MetaData.data.scanner.Detectors

import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.scanner.MovieScanResult

class MovieDetector {

    private val stopWords = setOf(

        "1080P",
        "720P",
        "2160P",
        "480P",

        "WEB",
        "WEB-DL",
        "WEBRIP",
        "HDRIP",
        "BDRIP",
        "BLURAY",
        "REMUX",

        "X264",
        "X265",
        "H264",
        "H265",
        "HEVC",

        "AAC",
        "AC3",
        "DD",
        "DDP",
        "TRUEHD",
        "ATMOS",

        "NF",
        "AMZN",
        "DSNP",

        "HINDI",
        "ENGLISH",
        "KOREAN",
        "HIN",
        "ENG",
        "KOR",

        "DUAL",
        "AUDIO",

        "ESUB",
        "MSUB",
        "MSUBS",
        "SUB",

        "HDR",
        "DV",

        "10BIT",
        "10BITS",

        "VEGAMOVIES",
        "VEGAMOVIES.IS"
    )

    private val yearRegex =
        Regex("^(19|20)\\d{2}$")

    fun detect(
        mediaFile: MediaFile
    ): MovieScanResult {

        val cleaned = cleanName(mediaFile.name)

        return MovieScanResult(

            title = cleaned.first,

            year = cleaned.second,

            filePath = mediaFile.uri.toString(),

            folderPath = mediaFile.parentFolder ?: ""

        )
    }

    private fun cleanName(
        fileName: String
    ): Pair<String, Int?> {

        val name = fileName
            .substringBeforeLast(".")
            .replace(".", " ")
            .replace("_", " ")
            .replace("-", " ")

        val words = mutableListOf<String>()

        var year: Int? = null

        for (word in name.split("\\s+".toRegex())) {

            val upper = word.uppercase()

            if (yearRegex.matches(word)) {

                year = word.toInt()

                break
            }

            if (upper in stopWords)
                break

            words.add(word)
        }

        return words.joinToString(" ").trim() to year
    }
}