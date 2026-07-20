package com.example.showcase.features.Player.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.HomeScreen.BootomBAR
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.TrackSheetType
import com.example.showcase.features.Player.repository.PlayerViewModelFactory
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.navigation.PlayerType
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay


//@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreenV(
    navController: NavHostController,
    mediaId: Long,
    mediaType: PlayerType
) {

    val playerViewModel: PlayerViewModel = viewModel(
        factory = PlayerViewModelFactory()
    )
    val state by playerViewModel.state.collectAsState()
    val metadata by playerViewModel.infoState.collectAsState()
    val isFullscreen by playerViewModel.isFullscreen.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(isLandscape) {
        playerViewModel.setFullscreen(isLandscape)
    }

    LaunchedEffect(mediaId, mediaType) {

        when (mediaType) {

            PlayerType.MOVIE ->
                playerViewModel.startMovie(mediaId)

            PlayerType.SERIES ->
                playerViewModel.startSeries(mediaId)
        }
    }

    val progress by ProgressManager
        .progress
        .collectAsState()


    if (!isFullscreen) {
        Box() {

            state.media?.poster?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .blur(7.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = VMocha.Blue.copy(0.3f)
                    )
            )

            Column(
                Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.padding(10.dp))
                Box(
                    Modifier
                        .padding(5.dp)
                        .background(VMocha.Crust, RoundedCornerShape(20.dp))

                ) {
                    Column() {
                        Box(
                            Modifier
                                .padding(horizontal = 5.dp)
                                .clip(shape = RoundedCornerShape(20.dp))
                        ) {
                            player(PlayerGraph.playerManager, playerViewModel, isFullscreen)
                        }
                        Spacer(Modifier.padding(5.dp))
                        MedialPlean(metadata, state, playerViewModel)
                    }

                }
                Spacer(Modifier.padding(5.dp))
                if (state.media?.type == MediaType.EPISODE) {
                    EpPanel(
                        state.playlist,
                        state.currentIndex,
                        onEpisodeClick = {
                            playerViewModel.playEpisode(it)

                        })
                }
                BootomBAR(navController, progress)
            }

        }
    } else {
        fullScreenPlayer(PlayerGraph.playerManager, playerViewModel, metadata, state)
    }

    val sheet by playerViewModel.trackSheet.collectAsState()

    if (sheet != TrackSheetType.NONE) {

        ModalBottomSheet(
            onDismissRequest = {
                playerViewModel.hideSheet()
            }
        ) {

            when (sheet) {

                TrackSheetType.AUDIO ->

                    AudioTrackSheet(playerViewModel)

                TrackSheetType.SUBTITLE ->

                    SubtitleTrackSheet(playerViewModel)

                else -> {}
            }
        }
    }
}

@Composable
fun player(
    playerManager: PlayerManager,
    playerViewModel: PlayerViewModel,
    isfullscreen: Boolean,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)
) {

    val state by playerViewModel.state.collectAsState()

    var controlsVisible by remember<MutableState<Boolean>> {
        mutableStateOf(true)
    }

    LaunchedEffect(controlsVisible, state.isPlaying) {

        if (!isfullscreen) { //if Screen In Vertical
            controlsVisible = false
        }
        if (controlsVisible && state.isPlaying) {

            delay(4000)

            controlsVisible = false
        }

    }
    Box(
        modifier = modifier

    ) {


        VideoPlayer(
            playerManager,
            Modifier
                .fillMaxWidth()
                .matchParentSize()
        )

        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {


            //^^player
            TopControllsV(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize().padding(horizontal = 10.dp),
                playerViewModel,
                playerManager,
                { controlsVisible = !controlsVisible },
            )
        }
        AnimatedVisibility(
            visible = controlsVisible,
            Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            BootomControlls(modifier = Modifier.align(Alignment.BottomCenter), playerViewModel)
        }
        //^^controls


        Box(
            Modifier
                .fillMaxHeight(0.7f)
                .width(750.dp)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { controlsVisible = !controlsVisible },

                        onDoubleTap = { offset ->

                            if (offset.x < size.width / 2) {
                                // Left half
                                playerManager.seekBackward()
                            } else {
                                // Right half
                                playerManager.seekForward()
                            }
                        })
                }
        )


    }
}


//@Preview
@Composable
fun EpPanel(
    playlist: List<PlayerMedia>,
    currentIndex: Int,
    onEpisodeClick: (Int) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                VMocha.Crust, shape = RoundedCornerShape(20.dp)
            )
            .padding(top = 20.dp, start = 5.dp, end = 5.dp)
    ) {

        itemsIndexed(playlist) { index, media ->
            EpCard(
                media = media,
                selected = index == currentIndex,
                onClick = {
                    onEpisodeClick(index)
                })
        }

    }

}

//@Preview
@Composable
private fun EpCard(media: PlayerMedia, selected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected)
                    VMocha.Lavender
                else
                    VMocha.Mantle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
    ) {
        Row(

        ) {

            AsyncImage(
                model = media.backdrop,
                contentDescription = "Ep_Thumb",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(140.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            //Spacer(Modifier.padding(horizontal = 10.dp))

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 0.dp, top = 10.dp, end = 5.dp, bottom = 10.dp)
            ) {
                Text(
                    media.title, // RATING
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VMocha.Text,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                    //.basicMarquee()
                    ,
                    maxLines = 2,
                    textAlign = TextAlign.Start

                )
                Text(
                    " S${media.seasonNumber} | Ep${media.episodeNumber}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    color = VMocha.Subtext1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 5.dp)
                    //.basicMarquee()
                    ,
                    textAlign = TextAlign.Start

                )
            }

        }
    }

}


fun formatTime(ms: Long): String {

    val totalSeconds = ms / 1000

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0)

        "%d:%02d:%02d".format(
            hours,
            minutes,
            seconds
        )
    else

        "%02d:%02d".format(
            minutes,
            seconds
        )
}

@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier
) {

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(700),
        label = ""
    )

    Box(
        modifier = modifier

    ) {

        Box(
            Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .background(
                    VMocha.Lavender, RoundedCornerShape(20.dp)
                )
        )
        {
            Box(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(100), color = VMocha.Blue
                    )
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun SeekBar(
    progress: Float,
    onProgressChanged: (Float) -> Unit,
    onSeekFinished: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var width by remember { mutableStateOf(1f) }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = ""
    )

    Box(
        modifier = modifier
            .height(20.dp)
            .onSizeChanged {
                width = it.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures(

                    onDragEnd = {
                        onSeekFinished(progress)
                    }

                ) { change, _ ->

                    val p = (change.position.x / width).coerceIn(0f, 1f)

                    onProgressChanged(p)
                }
            }
    ) {

        // Background
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    VMocha.Surface1, RoundedCornerShape(20.dp)
                )
        )

        // Filled section
        Box(
            Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .background(
                    VMocha.Lavender, RoundedCornerShape(20.dp)
                )
        )

        // Thumb
        Box(
            Modifier
                .offset {
                    IntOffset(
                        ((width - 20.dp.toPx()) * animatedProgress).toInt(), 0
                    )
                }
                .size(20.dp)
                .background(
                    VMocha.Blue, CircleShape
                )
        )
    }
}