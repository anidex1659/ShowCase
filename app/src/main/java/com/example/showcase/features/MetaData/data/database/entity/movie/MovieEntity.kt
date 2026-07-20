package com.example.showcase.features.MetaData.data.database.entity.movie

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity

@Entity(
    tableName = "movies",

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
data class MovieEntity(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val libraryId: Long,

    val title: String,

    val folderPath: String,

    val metadataFolder: String,

    val filePath: String,

    val tmdbId: Int? = null,

    val duration: Long = 0,

    val watched: Boolean = false,

    val progress: Long = 0,

    val lastPlayed: Long = 0,

    val addedDate: Long,

    val lastScan: Long
)