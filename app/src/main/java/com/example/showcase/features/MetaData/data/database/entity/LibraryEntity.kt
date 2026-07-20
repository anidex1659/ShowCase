package com.example.showcase.features.MetaData.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "libraries"
)
data class LibraryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String,
    val folderUri: String,
    val enabled: Boolean,
    val createdDate: Long,
    val lastScan: Long
)