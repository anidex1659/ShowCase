package com.example.showcase.features.Player.ui

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.core.ProgressManager.ProgressManager
import com.example.showcase.features.HomeScreen.ui.BootomBAR
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerCore.VlcPlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerEngine
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.TrackSheetType
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModelFactory
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.ui.Navigation.PlayerGraph
import com.example.showcase.ui.Navigation.PlayerType
import com.example.showcase.ui.Theme.VMocha
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
    val context = LocalContext.current
    val state by playerViewModel.state.collectAsState()
    val metadata by playerViewModel.infoState.collectAsState()
    val isFullscreen by playerViewModel.isFullscreen.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val playerEngine by playerViewModel.playerEngine.collectAsState()

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

    LaunchedEffect(
        playerEngine
    ) {
        if (playerEngine== PlayerEngine.VLC) Toast.makeText(context,"(HW) Failed - Switched To Software (SW) acceleration", Toast.LENGTH_SHORT).show()
    }

    val progress by ProgressManager
        .progress
        .collectAsState()


    if (!isFullscreen) {
        Box {
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
                    Column {
                        Box(
                            Modifier
                                .padding(horizontal = 5.dp)
                                .clip(shape = RoundedCornerShape(20.dp))
                        ) {
                            Player(
                                PlayerGraph.playerManager,
                                playerViewModel,
                                isFullscreen,
                                playerInfoState = metadata,
                                vlcPlayerManager=PlayerGraph.vlcPlayerManager
                            )
                        }
                        Spacer(Modifier.padding(5.dp))

                        MedialPlean(metadata, playerViewModel, playerEngine)
                    }

                }
                Spacer(Modifier.padding(5.dp))

                EpPanel(
                    state.playlist,
                    state.currentIndex,
                    onEpisodeClick = {
                        playerViewModel.playEpisode(it)

                    },
                    state,
                    metadata
                )

                BootomBAR(navController, progress)
            }

        }
    } else {
        FullScreenPlayer(PlayerGraph.playerManager, playerViewModel, metadata, state)
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
fun Player(
    playerManager: PlayerManager,
    playerViewModel: PlayerViewModel,
    isfullscreen: Boolean,
    playerInfoState: PlayerInfoState,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(220.dp),
    vlcPlayerManager: VlcPlayerManager
) {

    val state by playerViewModel.state.collectAsState()

    val playerEngine by playerViewModel.playerEngine.collectAsState()

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

        when (playerEngine) {
             PlayerEngine.EXO ->
            VideoPlayer(
                playerManager,
                Modifier
                    .fillMaxWidth()
                    .matchParentSize()
            )

            PlayerEngine.VLC ->
            VlcVideoPlayer(
                vlcPlayerManager,
                Modifier
                    .fillMaxSize()
            )
        }

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
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                playerViewModel,
                playerManager,
                playerInfoState,
            )
        }
        AnimatedVisibility(
            visible = controlsVisible,
            Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            BootomControlls(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(horizontal = 10.dp), playerViewModel,playerEngine
            )
        }
        //^^controls

        Box(
            Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(0.75f)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { controlsVisible = !controlsVisible },

                        onDoubleTap = { offset ->

                            if (offset.x < size.width / 2) {
                                // Left half
                                when (playerEngine) {
                                    PlayerEngine.EXO -> PlayerGraph.playerManager.seekBackward()
                                    PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.seekBackward()
                                }
                            } else {
                                // Right half
                                when (playerEngine) {
                                    PlayerEngine.EXO -> PlayerGraph.playerManager.seekForward()
                                    PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.seekForward()
                                }
                            }
                        })
                })

        AnimatedVisibility(
            visible = controlsVisible,
            Modifier.align(Alignment.Center),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CenterControlls(
                 playerViewModel, state,
                Modifier
                    .padding(bottom = 12.dp)
                    .align(Alignment.Center),
                playerEngine
            )
        }

    }
}


//@Preview
@Composable
fun EpPanel(
    playlist: List<PlayerMedia>,
    currentIndex: Int,
    onEpisodeClick: (Int) -> Unit,
    state: PlayerState,
    metadata: PlayerInfoState
) {
    var start by remember { mutableStateOf(false) }
    var startL by remember { mutableStateOf(false) }

    val hight by animateFloatAsState(
        targetValue = if (start) 0.9f else 0f, animationSpec = tween(1200), label = ""
    )
    val length by animateFloatAsState(
        targetValue = if (startL) 0.8f else 0f, animationSpec = tween(1200), label = ""
    )
    LaunchedEffect(start) {
        start = true
        if (start) {
            delay(1000)
            startL = true
        }
    }

    if (state.media?.type == MediaType.EPISODE) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(hight)
                .background(
                    VMocha.Crust, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(top = 1.dp, start = 5.dp, end = 5.dp)
            , horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(
                Modifier.fillMaxWidth().background(
                    VMocha.Mantle, shape = RoundedCornerShape(20.dp)
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    state.media.title + " | E" + state.media.episodeNumber,
                    color = VMocha.Text
                )
                Box(Modifier.fillMaxWidth()) {
                    IconButton(
                        {}, Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            Icons.Default.Sync,
                            "Refresh Episodes"
                        )
                    }
                }
            }
            LazyColumn(Modifier.fillMaxWidth()) {

                itemsIndexed(playlist) { index, media ->
                    EpCard(
                        media = media, selected = index == currentIndex, onClick = {
                            onEpisodeClick(index)
                        })
                }

            }
        }
    }
    else if (state.media?.type == MediaType.MOVIE)
    {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(hight)
            .background(
                VMocha.Crust, shape = RoundedCornerShape(20.dp)
            )
            .padding(top = 20.dp, start = 5.dp, end = 5.dp))
        {
            Column{
                Text(
                    text = metadata.movie?.metadata?.tagline?:"",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 2.sp
                    ),
                    color = VMocha.Text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                AsyncImage(
                    model = state.media.backdrop,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .border(1.dp, VMocha.Lavender, RoundedCornerShape(20.dp))
                )
                Box(Modifier.fillMaxWidth()){
                Divider(
                    Modifier
                        .padding(5.dp)
                        .clip(shape = RoundedCornerShape(20))
                        .fillMaxWidth(length)
                        .align(Alignment.Center),
                    thickness = 1.dp,
                    color = VMocha.Text
                )}

                Text(
                    text = metadata.movie?.metadata?.genres?.joinToString (" | ").toString() 	,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VMocha.Text,
                )
                Text(
                    text = metadata.movie?.metadata?.plot?:"",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    color = VMocha.Text,
                    style = TextStyle(
                        lineHeight = 17.sp
                    )
                )
            }
        }
    }

}

//@Preview
@Composable
fun EpCard(media: PlayerMedia, selected: Boolean, onClick: () -> Unit) {
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
                    media.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = VMocha.Text,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 2,
                    textAlign = TextAlign.Start

                )
                Text(
                    media.plot,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    color = VMocha.Subtext0.copy(0.5f),
                    style = TextStyle(
                        lineHeight = 12.sp
                    ),
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxSize(),
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
fun SeekBar(
    progress: Float,
    onProgressChanged: (Float) -> Unit,
    onSeekFinished: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var width by remember { mutableStateOf(1f) }
    var isDragging by remember {
        mutableStateOf(false)
    }

    var dragProgress by remember {
        mutableStateOf(progress)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = ""
    )

    LaunchedEffect(progress) {

        if (!isDragging) {
            dragProgress = progress
        }
    }

    Box(
        modifier = modifier
            .height(20.dp)

            .onSizeChanged {
                width = it.width.toFloat()
            }

            .pointerInput(Unit) {

                detectDragGestures(

                    onDragStart = { offset ->

                        isDragging = true

                        dragProgress = (offset.x / width).coerceIn(0f, 1f)

                        onProgressChanged(
                            dragProgress
                        )
                    },

                    onDragEnd = {

                        isDragging = false

                        onSeekFinished(
                            dragProgress
                        )
                    }

                ) { change, _ ->

                    val newProgress = (change.position.x / width).coerceIn(0f, 1f)

                    dragProgress = newProgress

                    onProgressChanged(
                        newProgress
                    )
                }
            }) {

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