package com.example.showcase.features.MediaPages


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.showcase.features.HomeScreen.repo.HomeRepository
import com.example.showcase.features.InfoPage.repository.InfoRepository

class MediaViewModelFactory(
    private val homeRepository: HomeRepository,
    private val infoRepository: InfoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {

            return MediaViewModel(
                homeRepository = homeRepository,
                infoRepository = infoRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}