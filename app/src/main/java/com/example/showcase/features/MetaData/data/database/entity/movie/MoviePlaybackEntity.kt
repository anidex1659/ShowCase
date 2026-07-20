package com.example.showcase.features.MetaData.data.database.entity.movie

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_playback",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MoviePlaybackEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val movieId: Long,

    val position: Long,

    val duration: Long,

    val lastPlayed: Long
)