package com.example.showcase.features.MetaData.data.database.entity.series

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity

@Entity(
    tableName = "series",
    foreignKeys = [
        ForeignKey(
            entity = LibraryEntity::class,
            parentColumns = ["id"],
            childColumns = ["libraryId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ]
)
data class SeriesEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val libraryId: Long,

    val title: String,

    val tmdbId: Int? = null,

    val folderPath: String, //uri not actual path

    val metadataFolder: String,

    val episodeCount: Int,

    val addedDate: Long,

    val lastScan: Long
)