package com.example.showcase.features.MetaData.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicEntity
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.series.EpisodeEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

data class SeriesComplete(

    @Embedded
    val series: SeriesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "seriesId"
    )
    val episodes: List<EpisodeEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "seriesId"
    )
    val artworks: List<ArtworkEntity>
)


data class MovieComplete(

    @Embedded
    val movie: MovieEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val artworks: List<MovieArtworkEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val playback: MoviePlaybackEntity?
)



data class MusicComplete(

    @Embedded
    val music: MusicEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "musicId"
    )
    val artworks: List<MusicArtworkEntity>
)
