package com.example.showcase.features.Player.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.ui.navigation.PlayerGraph

class PlayerViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return PlayerViewModel(
            PlayerGraph.playerManager,
            PlayerGraph.repository
        ) as T
    }
}