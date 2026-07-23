package com.example.showcase.features.MetaData.data.scanner.Scanners

import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.music.MusicEntity
import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.scanner.Detectors.MusicDetector

class MusicScanner(

    private val detector: MusicDetector,

    private val dao: ShowcaseDao
) {

    suspend fun scan(

        libraryId: Long,

        mediaFiles: List<MediaFile>
    ) {

        mediaFiles.forEach {

            processMusic(
                libraryId,
                it
            )
        }
    }

    private suspend fun processMusic(

        libraryId: Long,

        mediaFile: MediaFile
    ) {

        val result =
            detector.detect(mediaFile)

        val exists =
            dao.getMusicByTitle(result.title)

        if (exists != null)
            return

        dao.insertMusic(

            MusicEntity(

                libraryId = libraryId,

                title = result.title,

                artist = result.artist,

                album = result.album,

                folderPath = result.folderPath,

                filePath = result.filePath,

                addedDate = System.currentTimeMillis(),

                lastScan = System.currentTimeMillis()
            )
        )
    }
}