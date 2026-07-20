package com.example.showcase.features.MetaData.data.database.entity.music

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "music_playback",
    foreignKeys = [
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = ["id"],
            childColumns = ["musicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MusicPlaybackEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val musicId: Long,

    val position: Long,

    val duration: Long,

    val lastPlayed: Long
)