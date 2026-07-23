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


    private val _uiState =
        MutableStateFlow(
            EditArtworkUiState()
        )

    val uiState: StateFlow<EditArtworkUiState> =
        _uiState.asStateFlow()


    // ============================================================
    // LOAD SERIES
    // ============================================================

    fun loadSeriesArtwork(
        seriesId: Long
    ) {

        currentSeriesId = seriesId
        currentMovieId = null

        editingMovie = false

        viewModelScope.launch {

            try {

                val series =
                    dao.getSeriesById(
                        seriesId
                    ) ?: return@launch


                currentSeriesTmdbId =
                    series.tmdbId


                currentStorageLocation =
                    StorageLocationManager
                        .seriesStorageFolder(
                            series.folderPath
                        )


                loadCurrentSeriesArtwork()

                loadSeriesImages()

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
    // LOAD MOVIE
    // ============================================================

    fun loadMovieArtwork(
        movieId: Long
    ) {

        currentMovieId = movieId
        currentSeriesId = null

        editingMovie = true

        viewModelScope.launch {

            try {

                val movie =
                    dao.getMovieById(
                        movieId
                    ) ?: return@launch


                currentMovieTmdbId =
                    movie.tmdbId


                currentStorageLocation =
                    StorageLocationManager
                        .seriesStorageFolder(
                            movie.folderPath
                        )


                loadCurrentMovieArtwork()

                loadMovieImages()

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

    fun selectArtworkType(
        type: String
    ) {

        _uiState.update {

            it.copy(
                selectedType = type,
                selectedArtworkUrl = null,
                artworkResults = emptyList(),
                saved = false
            )
        }


        viewModelScope.launch {

            if (editingMovie) {

                loadCurrentMovieArtwork()

                loadMovieImages()

            } else {

                loadCurrentSeriesArtwork()

                loadSeriesImages()

            }
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


//    fun saveSeriesArtwork() {
//
//        val seriesId =
//            currentSeriesId
//                ?: return
//
//        val imageUrl =
//            _uiState.value.selectedArtworkUrl
//                ?: return
//
//        val storageLocation =
//            currentStorageLocation
//                ?: return
//
//        viewModelScope.launch {
//
//            _uiState.update {
//
//                it.copy(
//                    isSaving = true,
//                    error = null
//                )
//            }
//
//            try {
//
//                val success =
//                    artworkDownloader.replaceSeriesArtwork(
//
//                        seriesId =
//                            seriesId,
//
//                        artworkType =
//                            _uiState.value.selectedType,
//
//                        imageUrl =
//                            imageUrl,
//
//                        storageLocation =
//                            storageLocation
//
//                    )
//
//                if (success) {
//
//                    // Reload the newly saved local artwork
//                    loadCurrentSeriesArtwork()
//                }
//
//                _uiState.update {
//
//                    it.copy(
//
//                        isSaving =
//                            false,
//
//                        saved =
//                            success,
//
//                        selectedArtworkUrl =
//                            null,
//
//                        error =
//                            if (!success)
//                                "Failed to save artwork"
//                            else
//                                null
//
//                    )
//                }
//
//            } catch (e: Exception) {
//
//                _uiState.update {
//
//                    it.copy(
//
//                        isSaving =
//                            false,
//
//                        saved =
//                            false,
//
//                        error =
//                            e.message
//                                ?: "Failed to save artwork"
//
//                    )
//                }
//            }
//        }
//    }


    // ============================================================
    // LOAD CURRENT SERIES ARTWORK
    // ============================================================

    private suspend fun loadCurrentSeriesArtwork() {

        val seriesId =
            currentSeriesId
                ?: return


        val artwork =
            dao.getArtwork(

                seriesId = seriesId,

                type =
                    _uiState.value.selectedType

            )


        _uiState.update {

            it.copy(

                currentArtworkUri =
                    artwork?.uri,

                selectedArtworkUrl =
                    null,

                saved =
                    false

            )
        }
    }


    // ============================================================
    // LOAD CURRENT MOVIE ARTWORK
    // ============================================================

    private suspend fun loadCurrentMovieArtwork() {

        val movieId =
            currentMovieId
                ?: return


        val artwork =
            dao.getMovieArtwork(

                movieId = movieId,

                type =
                    _uiState.value.selectedType

            )


        _uiState.update {

            it.copy(

                currentArtworkUri =
                    artwork?.uri,

                selectedArtworkUrl =
                    null,

                saved =
                    false

            )
        }
    }


    // ============================================================
    // LOAD SERIES TMDB IMAGES
    // ============================================================

    private suspend fun loadSeriesImages() {

        val tmdbId =
            currentSeriesTmdbId
                ?: return


        _uiState.update {

            it.copy(
                isLoading = true,
                error = null
            )
        }


        try {

            val response =
                remote.getSeriesImages(
                    tmdbId
                )


            val images =

                when (
                    _uiState.value.selectedType
                ) {

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
                images.mapNotNull { image ->

                    remote.buildImageUrl(
                        image.filePath
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

                    artworkResults =
                        results,

                    isLoading =
                        false,

                    error =
                        null

                )
            }


        } catch (e: Exception) {

            _uiState.update {

                it.copy(

                    isLoading =
                        false,

                    error =
                        e.message

                )
            }
        }
    }


    // ============================================================
    // LOAD MOVIE TMDB IMAGES
    // ============================================================

    private suspend fun loadMovieImages() {

        val tmdbId =
            currentMovieTmdbId
                ?: return


        _uiState.update {

            it.copy(
                isLoading = true,
                error = null
            )
        }


        try {

            val response =
                remote.getMovieImages(
                    tmdbId
                )


            val images =

                when (
                    _uiState.value.selectedType
                ) {

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
                images.mapNotNull { image ->

                    remote.buildImageUrl(
                        image.filePath
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

                    artworkResults =
                        results,

                    isLoading =
                        false,

                    error =
                        null

                )
            }


        } catch (e: Exception) {

            _uiState.update {

                it.copy(

                    isLoading =
                        false,

                    error =
                        e.message

                )
            }
        }
    }


    // ============================================================
    // SAVE SERIES ARTWORK
    // ============================================================

    fun saveSeriesArtwork() {

        val seriesId =
            currentSeriesId
                ?: return


        val imageUrl =
            _uiState.value.selectedArtworkUrl
                ?: return


        val storageLocation =
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
                    artworkDownloader
                        .replaceSeriesArtwork(

                            seriesId =
                                seriesId,

                            artworkType =
                                _uiState.value.selectedType,

                            imageUrl =
                                imageUrl,

                            storageLocation =
                                storageLocation

                        )


                if (success) {

                    loadCurrentSeriesArtwork()

                }


                _uiState.update {

                    it.copy(

                        isSaving =
                            false,

                        saved =
                            success,

                        selectedArtworkUrl =
                            null,

                        error =
                            if (!success)
                                "Failed to save artwork"
                            else
                                null

                    )
                }


            } catch (e: Exception) {

                _uiState.update {

                    it.copy(

                        isSaving =
                            false,

                        error =
                            e.message

                    )
                }
            }
        }
    }


    // ============================================================
    // SAVE MOVIE ARTWORK
    // ============================================================

    fun saveMovieArtwork() {

        val movieId =
            currentMovieId
                ?: return


        val imageUrl =
            _uiState.value.selectedArtworkUrl
                ?: return


        val storageLocation =
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
                    artworkDownloader
                        .replaceMovieArtwork(

                            movieId =
                                movieId,

                            artworkType =
                                _uiState.value.selectedType,

                            imageUrl =
                                imageUrl,

                            storageLocation =
                                storageLocation

                        )


                if (success) {

                    loadCurrentMovieArtwork()

                }


                _uiState.update {

                    it.copy(

                        isSaving =
                            false,

                        saved =
                            success,

                        selectedArtworkUrl =
                            null,

                        error =
                            if (!success)
                                "Failed to save artwork"
                            else
                                null

                    )
                }


            } catch (e: Exception) {

                _uiState.update {

                    it.copy(

                        isSaving =
                            false,

                        error =
                            e.message

                    )
                }
            }
        }
    }


    // ============================================================
    // SAVE CURRENT ARTWORK
    // ============================================================

    fun saveArtwork() {

        if (editingMovie) {

            saveMovieArtwork()

        } else {

            saveSeriesArtwork()

        }
    }
}