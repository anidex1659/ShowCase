package com.example.showcase.features.Player.ui

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.showcase.features.Player.PlayerCore.PlayerManager

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    playerManager: PlayerManager,
    modifier: Modifier = Modifier
) {

    val resizeMode by playerManager.resizeMode.collectAsState()

    AndroidView(

        modifier = modifier,

        factory = { context ->

            PlayerView(context).apply {

                player = playerManager.player

                useController = false

                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        },

        update = {playerView ->

            playerView.player = playerManager.player


            playerView.resizeMode =
                when (resizeMode) {

                    PlayerManager.VideoResizeMode.FIT ->
                        AspectRatioFrameLayout.RESIZE_MODE_FIT

                    PlayerManager.VideoResizeMode.FILL ->
                        AspectRatioFrameLayout.RESIZE_MODE_FILL

                    PlayerManager.VideoResizeMode.ZOOM ->
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    PlayerManager.VideoResizeMode.STRETCH ->
                        AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                }
        }
    )
}