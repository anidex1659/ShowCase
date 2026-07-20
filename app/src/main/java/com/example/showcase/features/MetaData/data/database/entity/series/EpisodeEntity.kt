package com.example.showcase.features.MetaData.data.database.entity.series

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

@Entity(
    tableName = "episodes",
    foreignKeys = [
        ForeignKey(
            entity = SeriesEntity::class,
            parentColumns = ["id"],
            childColumns = ["seriesId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ]
)
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val seriesId: Long,

    val seasonNumber: Int = 1,

    val episodeNumber: Int,

    val title: String,

    val filePath: String,

    val duration: Long,

    val watched: Boolean,

    val progress: Long,

    val lastPlayed: Long
)