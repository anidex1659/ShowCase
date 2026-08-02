package com.example.showcase.core.ProgressManager

import com.example.showcase.features.Player.model.PlayerState

data class ScanProgress(

    val type: ProgressType = ProgressType.NONE,

    val visible: Boolean = false,

    val title: String = "",

    val subtitle: String = "",

    val progress: Float = 0f,

    val current: Int = 0,

    val total: Int = 0,

    val playerState: PlayerState? = null
)