package com.example.showcase.ui.popup.Dialogs.Model


data class ArtworkItem(
    val url: String,
    val type: String
)
data class EditArtworkUiState(

    val selectedType: String = "poster",

    val currentArtworkUri: String? = null,

    val selectedArtworkUrl: String? = null,

    val artworkResults: List<ArtworkItem> = emptyList(),

    val isLoading: Boolean = false,

    val isSaving: Boolean = false,

    val saved: Boolean = false,

    val error: String? = null

)

