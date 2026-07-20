package com.example.showcase.ui.popup.Dialogs.ViewModel

import androidx.lifecycle.ViewModel
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.ui.popup.Dialogs.Model.IdentifySeriesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeriesResult
import com.example.showcase.ui.popup.Dialogs.Repository.IdentifySeriesRepository
import kotlinx.coroutines.launch

class IdentifySeriesViewModel(

    private val remote:
    TmdbRemoteDataSource,

    private val repository:
    IdentifySeriesRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            IdentifySeriesUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    fun updateTitle(
        title: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                title = title
            )
    }

    fun search() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true
                )

            try {

                val results =
                    remote.searchSeries(
                        _uiState.value.title
                    )

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        results = results
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false
                    )
            }
        }
    }


    fun identifySeries(
        seriesId: Long
    ) {

        val tmdbId =
            _uiState.value.tmdbId
                ?: return

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true
                )

            try {

                repository.identifySeries(

                    seriesId =
                        seriesId,

                    tmdbId =
                        tmdbId
                )

                _uiState.value =
                    _uiState.value.copy(
                        loading = false
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false
                    )
            }
        }
    }


    fun selectResult(
        result: TmdbSeriesResult
    ) {

        _uiState.value =
            _uiState.value.copy(

                selectedResult = result,

                tmdbId = result.id,

                title = result.name
            )
    }
}