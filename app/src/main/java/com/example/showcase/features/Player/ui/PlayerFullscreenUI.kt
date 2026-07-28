package com.example.showcase.features.Player.ui

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerEngine
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay


@Composable
fun FullScreenPlayer(playerManager: PlayerManager, playerViewModel: PlayerViewModel, playerInfoState: PlayerInfoState,
                     state: PlayerState
) {
    val context = LocalContext.current

    var overlayVisible by remember<MutableState<Boolean>> {
        mutableStateOf(false)
    }



    LaunchedEffect(overlayVisible, state.isPlaying) {

        if (!overlayVisible && !state.isPlaying) {
            delay(2000)
            overlayVisible = true
        }
        if (state.isPlaying) {
            overlayVisible = false
        }
    }



    // Hide system status bar and navigation bar when entering full screen
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            // 1. Hide both status bar & navigation bar
            insetsController.hide(WindowInsetsCompat.Type.systemBars())

            // 2. Allow swiping edges to temporarily show controls without pushing layout
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            window?.let {
                val insetsController = WindowCompat.getInsetsController(it, it.decorView)
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    Box(
        Modifier.fillMaxSize()
    ){
        Player(
			playerManager,
			playerViewModel,
			isfullscreen = true,
			playerInfoState = playerInfoState,
			modifier = Modifier.fillMaxSize(),
            PlayerGraph.vlcPlayerManager
		)

        AnimatedVisibility(
            overlayVisible,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            FullScreenOverlay({
              playerViewModel.togglePlayPause()
                overlayVisible = false
            }, playerInfoState, state)
        }

    }

}

@Composable
fun FullScreenOverlay(
    Onclick: () -> Unit, playerInfoState: PlayerInfoState,
    state: PlayerState,
) {
    var Plot: String = playerInfoState.series?.metadata?.plot.toString()
    if (state.media?.plot != "") Plot = state.media?.plot ?: ""
    Box(
        Modifier
            .fillMaxSize()
            .clickable(onClick = Onclick)
            .background(color = VMocha.Crust.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
        ) {

            AsyncImage(
                model = state.media?.poster,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(20))
            )


            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {

                if (state.media?.type == MediaType.EPISODE) {
                    Text(
                        text = "You're Watching",
                        color = VMocha.Subtext1,
                        fontSize = 20.sp
                    )
                    Text(
                        text = playerInfoState.series?.metadata?.title ?: " ",
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = VMocha.Text,
                        fontSize = 35.sp
                    )
                    Text(
                        text = playerInfoState.series?.metadata?.originalTitle ?: " ",
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = VMocha.Subtext1,
                        fontSize = 15.sp
                    )
                    playerInfoState.series?.metadata?.year?.toString()?.let {
                        Text(
                            text = it + "|" + playerInfoState.series.metadata.rating,
                            fontWeight = FontWeight.Bold,
                            color = VMocha.Text,
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        text = state.media.title,
                        fontWeight = FontWeight.Bold,
                        color = VMocha.Text,
                        fontSize = 20.sp
                    )
                    Text(
                        text = Plot,
                        fontSize = 15.sp,
                        color = VMocha.Subtext1,
                        fontWeight = FontWeight.Bold,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = "You're Watching",
                        color = VMocha.Subtext1,
                        fontSize = 20.sp
                    )
                    Text(
                        text = playerInfoState.movie?.metadata?.title ?: " ",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        color = VMocha.Text,
                        fontSize = 35.sp
                    )
                    playerInfoState.movie?.metadata?.year?.toString()?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            color = VMocha.Text,
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        text = playerInfoState.movie?.metadata?.plot ?: " ",
                        fontSize = 15.sp,
                        color = VMocha.Subtext1,
                        fontWeight = FontWeight.Bold,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )

                }


            }

        }
    }

}




//@Preview
@Composable
fun TopControllsV(
    modifier: Modifier,
    playerViewModel: PlayerViewModel,
    playerManager: PlayerManager,
    playerInfoState: PlayerInfoState
) {

    val state by playerViewModel.state.collectAsState()
    val ractio by playerManager.resizeMode.collectAsState()
    val vlcractio by PlayerGraph.vlcPlayerManager.resizeMode.collectAsState()
    val playerEngine by playerViewModel.playerEngine.collectAsState()
    var spedUP by remember < MutableState<Boolean>>{
        mutableStateOf(false)
    }

    var EPDilog by remember<MutableState<Boolean>> {
        mutableStateOf(false)
    }


    Box(
        modifier = modifier
    ) {
       AnimatedVisibility(EPDilog,) {
            Epdilog(
                state.playlist,
                state.currentIndex,
                onEpisodeClick = { playerViewModel.playEpisode(it) },
                state,
                playerInfoState,
                onDismiss = {
                    EPDilog = false
                    playerViewModel.togglePlayPause()
                },
                m=Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        VMocha.Crust, shape = RoundedCornerShape(20.dp)
                    )
                    .padding(top = 20.dp, start = 5.dp, end = 5.dp))

        }

        Row(Modifier.fillMaxWidth()) {
            IconButton(onClick = { playerViewModel.toggleFullscreen() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    "",
                    tint = VMocha.Text
                )
            }
            if (state.media?.type == MediaType.EPISODE) {
                Row(
                    Modifier.padding(10.dp)
                ) {
                    Text(
                        playerInfoState.series?.metadata?.title ?: " ",
                        color = VMocha.Text,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        " | ",
                        color = VMocha.Text,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        state.media?.title ?: " ",
                        color = VMocha.Text,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )

                }

            } else {
                Text(
                    playerInfoState.movie?.metadata?.title ?: " ",
                    color = VMocha.Text,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

        }

        Column (
            Modifier.align(Alignment.CenterStart),
        ) {

            IconButton(
                onClick = {
                    playerViewModel.togglePlayerEngine()
                },
            ) {
                if (playerEngine== PlayerEngine.VLC){
                    Icon(
                        painter = painterResource(R.drawable.software),
                        contentDescription = null,
                        tint = VMocha.Red,
                        modifier = Modifier.scale(0.7f)
                    )
                }else{
                    Icon(
                        painter = painterResource(R.drawable.hardware),
                        contentDescription = null,
                        tint = VMocha.Green,
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }
            AutoRotationToggleButton()
            IconButton(
                //2X button
                onClick = {
                    if (spedUP) {
                        when (playerEngine) {
                            PlayerEngine.EXO -> PlayerGraph.playerManager.setPlaybackSpeed(1.0f)
                            PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.setPlaybackSpeed(1.0f)
                        }
                        spedUP = false
                    } else {
                        when (playerEngine) {
                            PlayerEngine.EXO -> PlayerGraph.playerManager.setPlaybackSpeed(2.0f)
                            PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.setPlaybackSpeed(2.0f)
                        }
                        spedUP = true
                    }
                },
            ) {
                if (spedUP) {
                    Icon(
                        painter = painterResource(R.drawable.twox),
                        contentDescription = null,
                        tint = VMocha.Red,
                        modifier = Modifier.scale(0.7f)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.twox),
                        contentDescription = null,
                        tint = VMocha.Text,
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }

        }

        //^^ Starting Controls======================================================================

        Column (
            Modifier.align(Alignment.CenterEnd),
        ) {
            IconButton(

                onClick = {playerViewModel.showSubtitleSheet()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.transcript),
                    contentDescription = null,
                    tint = VMocha.Text,
                    modifier = Modifier.scale(0.7f)
                )
            }

            IconButton(

                onClick = {playerViewModel.showAudioSheet()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.playlist),
                    contentDescription = null,
                    tint = VMocha.Text,
                    modifier = Modifier.scale(0.7f)
                )
            }

            IconButton(

                onClick = {
                    EPDilog = !EPDilog
                    playerViewModel.pause()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.songsfolder),
                    contentDescription = null,
                    tint = VMocha.Text,
                    modifier = Modifier.scale(0.7f)
                )
            }

            var SizeModeColor by remember<MutableState<Color>> {
                mutableStateOf(VMocha.Text)
            }
            if (playerEngine== PlayerEngine.EXO){
                if (ractio == PlayerManager.VideoResizeMode.FIT) {SizeModeColor = VMocha.Text}
                if (ractio == PlayerManager.VideoResizeMode.FILL) {SizeModeColor = VMocha.Red}
                if (ractio == PlayerManager.VideoResizeMode.ZOOM) {SizeModeColor = VMocha.Lavender}
                if (ractio == PlayerManager.VideoResizeMode.STRETCH) {SizeModeColor = VMocha.Green}
            }else{
                if (vlcractio == PlayerManager.VideoResizeMode.FIT) {SizeModeColor = VMocha.Text}
                if (vlcractio == PlayerManager.VideoResizeMode.FILL) {SizeModeColor = VMocha.Red}
                if (vlcractio == PlayerManager.VideoResizeMode.ZOOM) {SizeModeColor = VMocha.Lavender}
                if (vlcractio == PlayerManager.VideoResizeMode.STRETCH) {SizeModeColor = VMocha.Green}
            }



            IconButton(

                onClick = {
                    when (playerEngine) {
                        PlayerEngine.EXO ->
                            playerManager.cycleResizeMode()

                        PlayerEngine.VLC ->
                            PlayerGraph.vlcPlayerManager.cycleResizeMode()
                    }
                },
            ) {
                    Icon(
                        painter = painterResource(R.drawable.width),
                        contentDescription = null,
                        tint = SizeModeColor,
                        modifier = Modifier.scale(0.7f)
                    )

            }

            //^^ EndIng Controls====================================================================
        }

    }
}


@Composable
fun CenterControlls(
    playerViewModel: PlayerViewModel,
    state: PlayerState,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp),
    playerEngine: PlayerEngine
) {


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {


        IconButton(
            //10sec <<
            onClick = {
                when (playerEngine) {
                    PlayerEngine.EXO -> PlayerGraph.playerManager.seekBackward()
                    PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.seekBackward()
                }
            },

        ) {
            Icon(
                painter = painterResource(R.drawable.previous),
                contentDescription = null,
                tint = VMocha.Text,
                modifier = Modifier.scale(0.8f)
            )
        }


        IconButton(
            //last ep
            onClick = {playerViewModel.playPreviousEpisode()},
            // modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(R.drawable.previousep),
                contentDescription = null,
                tint = VMocha.Text,
                modifier = Modifier.scale(0.8f)
            )
        }

        IconButton(
            //play pause
            onClick = { when (playerEngine) {
                PlayerEngine.EXO -> PlayerGraph.playerManager.togglePlayPause()
                PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.togglePlayPause()
            }}
        ) {
            Icon(
                painter =
                    if (state.isPlaying){
                        painterResource(R.drawable.pause)}
                    else{
                        painterResource(R.drawable.play)},
                contentDescription = null,
                tint = VMocha.Text,
                modifier = Modifier.size(150.dp)

            )
        }

        IconButton(
            //next EP
            onClick = {playerViewModel.playNextEpisode()},
        ) {
            Icon(
                painter = painterResource(R.drawable.nextep),
                contentDescription = null,
                tint = VMocha.Text,
                modifier = Modifier.scale(0.8f)
            )
        }

        IconButton(
            //10sec >>
            onClick = {when (playerEngine) {
                PlayerEngine.EXO -> PlayerGraph.playerManager.seekForward()
                PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.seekForward()
            }},

        ) {
            Icon(
                painter = painterResource(R.drawable.next),
                contentDescription = null,
                tint = VMocha.Text,
                modifier = Modifier.scale(0.8f)
            )
        }


    }

}


//@Preview
@Composable
fun BootomControlls(
    modifier: Modifier,
    playerViewModel: PlayerViewModel,
    playerEngine: PlayerEngine
) {


    val state by when(playerEngine){
        PlayerEngine.VLC ->   PlayerGraph.vlcPlayerManager.playerState.collectAsState()
        PlayerEngine.EXO -> PlayerGraph.playerManager.playerState.collectAsState()
    }

    val player = PlayerGraph.playerManager


    val progress =

        if (state.duration > 0)

            state.currentPosition.toFloat() /
                    state.duration

        else

            0f

    Box(
        modifier = modifier
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)

        ) {
            val progress = if (state.duration > 0L) {
                state.currentPosition.toFloat() / state.duration.toFloat()
            } else {
                0f
            }
            SeekBar(
                progress,
                onProgressChanged = { newProgress ->
                    // Optional: update preview position
                },
                onSeekFinished = { finalProgress ->

                    playerViewModel.seekToProgress(
                        finalProgress
                    )

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f)
                    .height(10.dp)
                    .background(
                        VMocha.Surface1, RoundedCornerShape(20.dp)
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
                //.padding(vertical = 5.dp, horizontal = 10.dp)
            ) {
                Text(
                    formatTime(state.currentPosition)+" / "+formatTime(state.duration),
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                    color = VMocha.Text,
                    textAlign = TextAlign.End
                )

            }
        }

    }
}


@Composable
private fun Epdilog(
    playlist: List<PlayerMedia>,
    currentIndex: Int,
    onEpisodeClick: (Int) -> Unit,
    state: PlayerState,
    metadata: PlayerInfoState,
    onDismiss: () -> Unit,
    m: Modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(
            VMocha.Crust, shape = RoundedCornerShape(20.dp)
        )
        .padding(top = 20.dp, start = 5.dp, end = 5.dp)

) {
    Dialog(onDismissRequest = onDismiss, ) {
        if (state.media?.type == MediaType.EPISODE) {
            LazyColumn(
                modifier = m
            ) {

                itemsIndexed(playlist) { index, media ->
                    EpCard(
                        media = media, selected = index == currentIndex, onClick = {
                            onEpisodeClick(index)
                        })
                }

            }
        }
        else if (state.media?.type == MediaType.MOVIE)
        {
            Box(modifier = m)
            {
                Column() {
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
                        model = state.media?.backdrop,
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
                                .fillMaxWidth()
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
}

