package com.example.showcase.ui.navigation

import android.content.Context
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.repository.PlayerRepository

object PlayerGraph {

    lateinit var playerManager: PlayerManager
        private set

    lateinit var repository: PlayerRepository
        private set

    fun initialize(
        context: Context,
        repository: PlayerRepository
    ) {
        if (::playerManager.isInitialized) return

        playerManager = PlayerManager(context)
        this.repository = repository
    }
}