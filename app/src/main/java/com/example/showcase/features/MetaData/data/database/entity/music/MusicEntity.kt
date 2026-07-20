package com.example.showcase.features.MetaData.data.database.entity.music

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity

@Entity(
    tableName = "music",

    foreignKeys = [

        ForeignKey(
            entity = LibraryEntity::class,
            parentColumns = ["id"],
            childColumns = ["libraryId"],
            onDelete = ForeignKey.Companion.CASCADE
        )

    ],

    indices = [Index("libraryId")]
)
data class MusicEntity(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val libraryId: Long,

    val title: String,

    val artist: String? = null,

    val album: String? = null,

    val folderPath: String,

    val filePath: String,

    val duration: Long = 0,

    val addedDate: Long,

    val lastScan: Long
)