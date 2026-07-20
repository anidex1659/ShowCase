package com.example.showcase.features.Library.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.Library.state.LibraryUiState
import com.example.showcase.features.Library.repository.LibraryRepository

import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(

    private val repository: LibraryRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            LibraryUiState()
        )

    val uiState: StateFlow<LibraryUiState> =
        _uiState.asStateFlow()

    init {
        loadLibraries()
    }

    fun loadLibraries() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true,
                    error = null
                )

            try {

                val libraries =
                    repository.getLibraries()


                _uiState.value =
                    _uiState.value.copy(
                        libraries = libraries,
                        loading = false
                    )

                ProgressManager.hide()

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        error = e.message
                    )
            }
        }
    }

    fun addLibrary(
        library: LibraryEntity
    ) {

        viewModelScope.launch {

            try {

                repository.addLibrary(library)

                loadLibraries()

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        error = e.message
                    )
            }
        }
    }

    fun deleteLibrary(
        id: Long
    ) {

        viewModelScope.launch {

            try {

                repository.deleteLibrary(id)

                loadLibraries()

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        error = e.message
                    )
            }
        }
    }

    fun scanAllLibraries(
        context: Context
    ) {

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        loading = true,
                        error = null
                    )

                repository.scanAllLibraries(context)


                loadLibraries()


            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        error = e.message
                    )
            }
        }
    }

    fun scanLibrary(
        context: Context,
        libraryName: String
    ) {

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        loading = true,
                        error = null
                    )


                repository.scanLibraryByName(
                    context,
                    libraryName
                )


                loadLibraries()




            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        error = e.message
                    )
            }
        }
    }

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }
}