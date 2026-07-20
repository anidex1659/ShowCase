package com.example.showcase.features.MediaPages


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.HomeScreen.repo.HomeRepository
import com.example.showcase.features.InfoPage.repository.InfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaViewModel(

    private val homeRepository: HomeRepository,

    private val infoRepository: InfoRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaUiState())
    val uiState = _uiState.asStateFlow()

    fun load(type: MediaType) {
        when (type) {
            MediaType.MOVIE -> loadMovies()
            MediaType.SERIES -> loadSeries()
        }
    }

    private fun loadMovies() {

        viewModelScope.launch {

            val movies = homeRepository
                .getTrendingMovies()
                .map { movie ->

                    val card = movie.toMediaCard()

                    val info = infoRepository.loadMovie(card.id)

                    card.copy(
                        plot = info.metadata?.plot,
                        tagline = info.metadata?.tagline,
                        genres = info.metadata?.genres ?: emptyList(),
                        rating = info.metadata?.rating?.toFloat(),
                        year = info.metadata?.releaseDate?.take(4),
                        runtime = info.metadata?.runtime
                    )
                }

            _uiState.update {
                it.copy(
                    media = movies,
                    hero = movies.firstOrNull(),
                    selectedType = MediaType.MOVIE,
                    loading = false
                )
            }
        }
    }

    private fun loadSeries() {

        viewModelScope.launch {

            val series = homeRepository
                .getTrendingSeries()
                .map { show ->

                    val card = show.toMediaCard()

                    val info = infoRepository.loadSeries(card.id)

                    card.copy(
                        plot = info.metadata?.plot,
                        tagline = info.metadata?.title,
                        genres = info.metadata?.genres ?: emptyList(),
                        rating = info.metadata?.rating,
                        year = info.metadata?.year.toString(),
                        runtime = null
                    )
                }

            _uiState.update {
                it.copy(
                    media = series,
                    hero = series.firstOrNull(),
                    selectedType = MediaType.SERIES,
                    loading = false
                )
            }
        }
    }
}