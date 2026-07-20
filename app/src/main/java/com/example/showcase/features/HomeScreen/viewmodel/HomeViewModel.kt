package com.example.showcase.features.HomeScreen.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.HomeScreen.repo.HomeRepository
import com.example.showcase.features.HomeScreen.uistate.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(

    private val repository: HomeRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            HomeUiState()
        )

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {

        refresh()
    }

    fun refresh() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true,
                    error = null
                )

            try {

                _uiState.value =
                    HomeUiState(

                        loading = false,

                        hero =
                            repository.getHeroItems(),

                        continueWatching =
                            repository.getContinueWatching(),

                        trendingSeries =
                            repository.getTrendingSeries(),

                        trendingMovies =
                            repository.getTrendingMovies(),

                        recentlyAddedSeries =
                            repository.getRecentlyAddedSeries(),

                        recentlyAddedMovies =
                            repository.getRecentlyAddedMovies()
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(

                        loading = false,

                        error = e.message
                    )
            }
        }
    }
}