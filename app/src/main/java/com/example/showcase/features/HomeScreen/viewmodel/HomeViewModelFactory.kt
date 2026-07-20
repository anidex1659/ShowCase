package com.example.showcase.features.HomeScreen.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.showcase.features.HomeScreen.repo.HomeRepository

class HomeViewModelFactory(

    private val repository: HomeRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(
            repository
        ) as T
    }
}