package com.example.showcase.features.MetaData.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity

data class LibraryComplete(
    @Embedded
    val library: LibraryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "libraryId"
    )
    val series: List<SeriesEntity>
)
