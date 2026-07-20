package com.example.showcase.features.MetaData.data.repository.metadata.sires

import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.series.PlaybackHistoryEntity

class PlaybackRepository(

    private val dao: ShowcaseDao

) {

    suspend fun getContinueWatching():
            List<PlaybackHistoryEntity> {

        return dao.getContinueWatching()
    }

    suspend fun getPlaybackHistory():
            List<PlaybackHistoryEntity> {

        return dao.getPlaybackHistory()
    }

    suspend fun savePlayback(

        mediaId: Long,

        mediaType: String,

        position: Long,

        duration: Long

    ) {

        dao.insertPlayback(

            PlaybackHistoryEntity(

                mediaId = mediaId,

                mediaType = mediaType,

                position = position,

                duration = duration,

                lastPlayed = System.currentTimeMillis()

            )

        )
    }
}