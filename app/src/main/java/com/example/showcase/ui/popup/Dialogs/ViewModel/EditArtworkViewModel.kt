package com.example.showcase.ui.popup.Dialogs.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.Player.model.storage.StorageLocationManager
import com.example.showcase.ui.popup.Dialogs.Model.ArtworkItem
import com.example.showcase.ui.popup.Dialogs.Model.EditArtworkUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity

class EditArtworkViewModel(
    private val dao: ShowcaseDao,
    private val remote: TmdbRemoteDataSource,
    private val artworkDownloader: ArtworkDownloader
) : ViewModel() {

    private var currentSeriesId: Long? = null
    private var currentMovieId: Long? = null

    private var currentSeriesTmdbId: Int? = null
    private var currentMovieTmdbId: Int? = null

    private var currentStorageLocation: String? = null

    private var editingMovie = false

    private inline fun <T> whenMedia(
        series: () -> T,
        movie: () -> T
    ): T {
        return if (editingMovie) movie() else series()
    }
    private val _uiState =
        MutableStateFlow(
            EditArtworkUiState()
        )

    val uiState: StateFlow<EditArtworkUiState> =
        _uiState.asStateFlow()


    // ============================================================
    // LOAD SERIES
    // ============================================================

    fun loadSeriesArtwork(seriesId: Long) {

        editingMovie = false
        currentSeriesId = seriesId
        currentMovieId = null

        viewModelScope.launch {

            try {

                val series =
                    dao.getSeriesById(seriesId)
                        ?: return@launch

                currentSeriesTmdbId = series.tmdbId

                currentStorageLocation =
                    StorageLocationManager.seriesStorageFolder(
                        series.folderPath
                    )

                loadCurrentArtwork()
                loadTmdbImages()

            } catch (e: Exception) {

                _uiState.update {

                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun loadMovieArtwork(movieId: Long) {

        editingMovie = true
        currentMovieId = movieId
        currentSeriesId = null

        viewModelScope.launch {

            try {

                val movie =
                    dao.getMovieById(movieId)
                        ?: return@launch

                currentMovieTmdbId = movie.tmdbId

                currentStorageLocation =
                    StorageLocationManager.seriesStorageFolder(
                        movie.folderPath
                    )

                loadCurrentArtwork()
                loadTmdbImages()

            } catch (e: Exception) {

                _uiState.update {

                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    // ============================================================
    // CHANGE ARTWORK TYPE
    // ============================================================
    fun selectArtworkType(type: String) {

        _uiState.update {

            it.copy(

                selectedType = type,

                selectedArtworkUrl = null,

                artworkResults = emptyList(),

                saved = false
            )
        }

        viewModelScope.launch {

            loadCurrentArtwork()

            loadTmdbImages()
        }
    }

    // ============================================================
    // SELECT ARTWORK
    // ============================================================

    fun selectArtwork(
        url: String
    ) {

        _uiState.update {

            it.copy(
                selectedArtworkUrl = url,
                saved = false,
                error = null
            )
        }
    }

    // ============================================================
    // LOAD CURRENT SERIES ARTWORK
    // ============================================================
    private suspend fun loadCurrentArtwork() {

        val currentArtworkUri =
            if (editingMovie) {

                val movieId =
                    currentMovieId ?: return

                dao.getMovieArtwork(
                    movieId,
                    _uiState.value.selectedType
                )?.uri

            } else {

                val seriesId =
                    currentSeriesId ?: return

                dao.getArtwork(
                    seriesId,
                    _uiState.value.selectedType
                )?.uri
            }

        _uiState.update {

            it.copy(
                currentArtworkUri = currentArtworkUri,
                selectedArtworkUrl = null,
                saved = false
            )
        }
    }
    // ============================================================
    // LOAD SERIES TMDB IMAGES
    // ============================================================

    private suspend fun loadTmdbImages() {

        val tmdbId =

            if (editingMovie)
                currentMovieTmdbId
            else
                currentSeriesTmdbId

        tmdbId ?: return

        _uiState.update {

            it.copy(
                isLoading = true,
                error = null
            )
        }

        try {

            val response =

                if (editingMovie)
                    remote.getMovieImages(tmdbId)
                else
                    remote.getSeriesImages(tmdbId)

            val images =

                when (_uiState.value.selectedType) {

                    "poster" ->
                        response.posters

                    "backdrop" ->
                        response.backdrops

                    "logo" ->
                        response.logos

                    else ->
                        emptyList()
                }

            val results =

                images.mapNotNull {

                    remote.buildImageUrl(
                        it.filePath
                    )?.let { url ->

                        ArtworkItem(

                            url = url,

                            type =
                                _uiState.value.selectedType
                        )
                    }
                }

            _uiState.update {

                it.copy(

                    artworkResults = results,

                    isLoading = false,

                    error = null
                )
            }

        } catch (e: Exception) {

            _uiState.update {

                it.copy(

                    isLoading = false,

                    error = e.message
                )
            }
        }
    }

    // ============================================================
    // SAVE SERIES ARTWORK
    // ============================================================

    fun saveArtwork() {

        val imageUrl =
            _uiState.value.selectedArtworkUrl
                ?: return

        val storage =
            currentStorageLocation
                ?: return

        viewModelScope.launch {

            _uiState.update {

                it.copy(
                    isSaving = true,
                    error = null
                )
            }

            try {

                val success =

                    if (editingMovie) {

                        artworkDownloader.replaceMovieArtwork(

                            movieId =
                                currentMovieId ?: return@launch,

                            artworkType =
                                _uiState.value.selectedType,

                            imageUrl = imageUrl,

                            storageLocation = storage
                        )

                    } else {

                        artworkDownloader.replaceSeriesArtwork(

                            seriesId =
                                currentSeriesId ?: return@launch,

                            artworkType =
                                _uiState.value.selectedType,

                            imageUrl = imageUrl,

                            storageLocation = storage
                        )
                    }

                if (success) {

                    loadCurrentArtwork()
                }

                _uiState.update {

                    it.copy(

                        isSaving = false,

                        saved = success,

                        selectedArtworkUrl = null,

                        error =
                            if (success)
                                null
                            else
                                "Failed to save artwork"
                    )
                }

            } catch (e: Exception) {

                _uiState.update {

                    it.copy(

                        isSaving = false,

                        error = e.message
                    )
                }
            }
        }
    }
}