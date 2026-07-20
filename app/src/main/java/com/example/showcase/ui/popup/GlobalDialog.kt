package com.example.showcase.ui.popup

sealed class GlobalDialog {

    data class EditArtwork(
        val seriesId: Long
    ) : GlobalDialog()

    data class EditSeriesMetadata(
        val seriesId: Long
    ) : GlobalDialog()

    data class IdentifySeries(
        val seriesId: Long
    ) : GlobalDialog()

    object None : GlobalDialog()
}