package com.example.showcase.core.ProgressManager


import com.example.showcase.features.Player.model.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
enum class ProgressType {

    NONE,

    SCANNING,

    PLAYING
}

object ProgressManager {

    private val _progress =
        MutableStateFlow(
            ScanProgress()
        )

    val progress: StateFlow<ScanProgress> =
        _progress.asStateFlow()


    // =========================================================
    // SCANNING
    // =========================================================

    fun update(
        title: String,
        subtitle: String,
        current: Int,
        total: Int
    ) {

        _progress.value = ScanProgress(

            type = ProgressType.SCANNING,

            visible = true,

            title = title,

            subtitle = subtitle,

            current = current,

            total = total,

            progress =
                if (total == 0)
                    0f
                else
                    current.toFloat() / total
        )
    }


    fun show(
        title: String,
        subtitle: String = "",
        backdrop: String? = ""
    ) {

        _progress.value = ScanProgress(

            type = ProgressType.SCANNING,

            visible = true,

            title = title,

            subtitle = subtitle
        )
    }


    // =========================================================
    // PLAYER
    // =========================================================

    fun updatePlayer(
        playerState: PlayerState
    ) {

        _progress.value = ScanProgress(

            type = ProgressType.PLAYING,

            visible = true,

            playerState = playerState
        )
    }


    // =========================================================
    // HIDE
    // =========================================================

    fun hide() {

        _progress.value =
            ScanProgress()
    }
}