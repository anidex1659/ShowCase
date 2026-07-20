package com.example.showcase.features.MetaData.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

data class SeriesWithArtwork(

    @Embedded
    val series: SeriesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "seriesId"
    )
    val artwork: List<ArtworkEntity>
)
