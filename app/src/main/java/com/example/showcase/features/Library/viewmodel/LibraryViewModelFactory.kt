package com.example.showcase.features.Library.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.showcase.features.Library.repository.LibraryRepository

class LibraryViewModelFactory(

    private val repository: LibraryRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        @Suppress("UNCHECKED_CAST")
        return LibraryViewModel(
            repository
        ) as T
    }
}