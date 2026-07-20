package com.example.showcase.features.MetaData.data.database.entity.movie

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_artwork",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieArtworkEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val movieId: Long,

    val type: String,

    val fileName: String,

    val available: Boolean,

    val uri: String,

    val source: String
)