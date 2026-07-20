package com.example.showcase.features.MetaData.data.database.entity.series

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playback_history",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["mediaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class PlaybackHistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mediaId : Long,
    val mediaType : String,
    val position: Long,
    val duration: Long,
    val lastPlayed: Long
)