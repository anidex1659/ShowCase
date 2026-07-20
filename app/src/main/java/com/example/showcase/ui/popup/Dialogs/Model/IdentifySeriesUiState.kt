package com.example.showcase.ui.popup.Dialogs.Model

import com.example.showcase.features.MetaData.data.remote.model.TmdbSeriesResult

data class IdentifySeriesUiState(

    val title: String = "",

    val tmdbId: Int? = null,

    val selectedResult: TmdbSeriesResult? = null,

    val results: List<TmdbSeriesResult> = emptyList(),

    val loading: Boolean = false
)