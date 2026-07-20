package com.example.showcase.features.MetaData.data.database.entity.music

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "music_artwork",
    foreignKeys = [
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = ["id"],
            childColumns = ["musicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MusicArtworkEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val musicId: Long,

    val type: String,

    val fileName: String,

    val available: Boolean,

    val uri: String,

    val source: String
)