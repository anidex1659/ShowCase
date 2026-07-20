package com.example.showcase.features.MetaData.data.database.entity.series

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

@Entity(
    tableName = "artworks",
    foreignKeys = [
        ForeignKey(
            entity = SeriesEntity::class,
            parentColumns = ["id"],
            childColumns = ["seriesId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("seriesId")]
)
data class ArtworkEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val seriesId: Long,
    val type: String,           // poster, backdrop, banner, logo, thumb, metadata
    val fileName: String,       // poster.jpg
    val available: Boolean,
    val uri: String?,           // SAF Uri
    val source: String = "TMDB",// TMDB / User / FanArtTV
    val lastUpdated: Long = System.currentTimeMillis()
)