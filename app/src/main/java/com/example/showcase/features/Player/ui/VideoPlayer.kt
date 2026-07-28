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
import android.graphics.Color
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.media3.ui.CaptionStyleCompat
import com.example.showcase.features.Player.PlayerCore.VlcPlayerManager
import com.example.showcase.ui.theme.VMocha

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    playerManager: PlayerManager,
    modifier: Modifier = Modifier
) {

    val resizeMode by playerManager.resizeMode.collectAsState()

    val subtitleStyle = remember {
        CaptionStyleCompat(
            Color.WHITE,
            Color.TRANSPARENT,
            Color.TRANSPARENT,
            CaptionStyleCompat.EDGE_TYPE_OUTLINE,
            Color.BLACK,
            null
        )
    }

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

            playerView.subtitleView?.setStyle(
                subtitleStyle
            )
        }
    )
}
@Composable
fun VlcVideoPlayer(
    vlcPlayerManager: VlcPlayerManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Collect the exact same resizeMode StateFlow
    val resizeMode by vlcPlayerManager.resizeMode.collectAsState()

    val textureView = remember {
        TextureView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    DisposableEffect(textureView) {
        vlcPlayerManager.attachTextureView(textureView)
        onDispose {
            vlcPlayerManager.detachTextureView()
        }
    }

    AndroidView(
        factory = { textureView },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            // Pass view dimensions so LibVLC can calculate crop/stretch bounds
            vlcPlayerManager.applyResizeMode(resizeMode, view.width, view.height)
        }
    )
}