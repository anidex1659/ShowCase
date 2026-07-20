package com.example.showcase.features.Library.state

import com.example.showcase.core.progresmanager.ScanProgress
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.model.LibraryUiModel

data class LibraryUiState(

    val libraries: List<LibraryUiModel> = emptyList(),

    val loading: Boolean = false,

    val error: String? = null,

    val scanProgress: ScanProgress = ScanProgress()
)


