package com.example.showcase.features.MetaData.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.series.EpisodeEntity
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicPlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.series.PlaybackHistoryEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

@Database(
    entities = [

        LibraryEntity::class,

        SeriesEntity::class,
        EpisodeEntity::class,
        ArtworkEntity::class,
        PlaybackHistoryEntity::class,

        MovieEntity::class,
        MovieArtworkEntity::class,
        MoviePlaybackEntity::class,

        MusicEntity::class,
        MusicArtworkEntity::class,
        MusicPlaybackEntity::class


    ],
    version = 8
)
abstract class ShowcaseDatabase
    : RoomDatabase() {

    abstract fun dao():
            ShowcaseDao
}