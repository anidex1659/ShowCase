package com.example.showcase.features.InfoPage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.InfoPage.repository.InfoRepository
import com.example.showcase.features.InfoPage.uistate.InfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoViewModel(

    private val repository: InfoRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            InfoUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    fun loadSeries(
        id: Long
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(

                    loading = true,

                    error = null
                )

            runCatching {

                repository.loadSeries(id)

            }.onSuccess {

                _uiState.value =
                    InfoUiState(

                        loading = false,

                        series = it
                    )

            }.onFailure {

                _uiState.value =
                    _uiState.value.copy(

                        loading = false,

                        error = it.message
                    )
            }
        }
    }

    fun loadMovie(
        id: Long
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(

                    loading = true,

                    error = null
                )

            runCatching {

                repository.loadMovie(id)

            }.onSuccess {

                _uiState.value =
                    InfoUiState(

                        loading = false,

                        movie = it
                    )

            }.onFailure {

                _uiState.value =
                    _uiState.value.copy(

                        loading = false,

                        error = it.message
                    )
            }
        }
    }
}