package com.example.showcase.features.MetaData.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.showcase.features.MetaData.data.database.entity.series.EpisodeEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

data class SeriesWithEpisodes(

    @Embedded
    val series: SeriesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "seriesId"
    )
    val episodes: List<EpisodeEntity>
)