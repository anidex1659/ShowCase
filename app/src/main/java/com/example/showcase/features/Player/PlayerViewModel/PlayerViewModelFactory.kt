package com.example.showcase.features.Player.PlayerViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.showcase.ui.Navigation.PlayerGraph

class PlayerViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return PlayerViewModel(
            PlayerGraph.playerManager, PlayerGraph.vlcPlayerManager,
            PlayerGraph.repository,

            ) as T
    }
}