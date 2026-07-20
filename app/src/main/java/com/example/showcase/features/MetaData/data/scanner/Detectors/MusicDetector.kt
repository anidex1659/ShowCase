package com.example.showcase.features.MetaData.data.scanner.Detectors

import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.scanner.MusicScanResult

class MusicDetector {

    fun detect(
        mediaFile: MediaFile
    ): MusicScanResult {

        val title =
            mediaFile.name
                .substringBeforeLast(".")

        return MusicScanResult(

            title = title,

            artist = null,

            album = null,

            filePath = mediaFile.uri.toString(),

            folderPath = mediaFile.parentFolder ?: ""
        )
    }
}