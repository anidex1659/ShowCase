package com.example.showcase.features.Player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showcase.R
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay




@Composable
fun fullScreenPlayer(playerManager: PlayerManager, playerViewModel: PlayerViewModel, playerInfoState: PlayerInfoState,
                     state: PlayerState
) {

    var overlayVisible by remember<MutableState<Boolean>> {
        mutableStateOf(false)
    }

    LaunchedEffect(overlayVisible, state.isPlaying) {

        if (!overlayVisible && !state.isPlaying) {
            delay(2000)
            overlayVisible = true
        }
    }

    Box(
        Modifier.fillMaxSize()
    ){
        player(playerManager, playerViewModel, modifier = Modifier.fillMaxSize(), isfullscreen = true)
        if (overlayVisible)
            fullScreenOverlay({
                playerManager.togglePlayPause()
                overlayVisible = false
            }, playerInfoState, state)
    }

}

@Composable
 fun fullScreenOverlay(
    Onclick: () -> Unit, playerInfoState: PlayerInfoState,
    state: PlayerState
) {
    Box(
        Modifier
            .fillMaxSize()
            .clickable(onClick = Onclick)
            .background(color = VMocha.Crust.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
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
                        text = it + "|" + playerInfoState.series.metadata.rating ,
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
                    text = playerInfoState.series?.metadata?.plot ?: " ",
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
                    fontWeight = FontWeight.ExtraBold, overflow = TextOverflow.Ellipsis,
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




//@Preview
@Composable
fun TopControllsV(
    modifier: Modifier,
    playerViewModel: PlayerViewModel,
    playerManager: PlayerManager,
    ontap: ((Offset) -> Unit)?, ) {


    val isFullscreen by playerViewModel.isFullscreen.collectAsState()

    Box(
        modifier = modifier
    ) {


        Row(
            Modifier.align(Alignment.TopStart)

        ) {
            IconButton(
                //Flip {full screen}
                onClick = {
                    playerViewModel.toggleFullscreen()
                },
            ) {
                if (isFullscreen) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = VMocha.Text,
                        modifier = Modifier.scale(0.7f)
                    )
                } else {
                    Icon(
                        Icons.Default.FitScreen,
                        contentDescription = null,
                        tint = VMocha.Text,
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }


        }
        Column (
            Modifier.align(Alignment.CenterStart),
        ) {
            IconButton(
                //Flip {full screen}
                onClick = {
                    playerViewModel.toggleFullscreen()
                },
            ) {
                if (isFullscreen) {
                    Icon(
                        painter = painterResource(R.drawable.twox),
                        contentDescription = null,
                        tint = VMocha.Text,
                        modifier = Modifier.scale(0.7f)
                    )
                } else {
                    Icon(
                        Icons.Default.FitScreen,
                        contentDescription = null,
                        tint = VMocha.Text,
                        modifier = Modifier.scale(0.7f)
                    )
                }
            }
        }
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

                onClick = {playerViewModel.showAudioSheet()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.songsfolder),
                    contentDescription = null,
                    tint = VMocha.Text,
                    modifier = Modifier.scale(0.7f)
                )
            }

            IconButton(

                onClick = {playerManager.cycleResizeMode()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.width),
                    contentDescription = null,
                    tint = VMocha.Text,
                    modifier = Modifier.scale(0.7f)
                )
            }


        }




    }
}


//@Preview
@Composable
fun BootomControlls(modifier: Modifier, playerViewModel: PlayerViewModel) {


    val state by PlayerGraph.playerManager.playerState.collectAsState()
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
            Box(
                Modifier
                    .background(VMocha.Mantle.copy(0.5f), shape = RoundedCornerShape(20.dp, 20.dp))
                    .fillMaxWidth()
                    .height(30.dp)
            )

            {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                    //.padding(vertical = 5.dp, horizontal = 10.dp)
                ) {
                    Text(
                        formatTime(state.currentPosition),
                        modifier = Modifier.padding(start =5.dp, end = 5.dp),
                        color = VMocha.Text,
                        textAlign = TextAlign.End
                    )
                    var seekProgress by remember {
                        mutableFloatStateOf(progress)
                    }

                    SeekBar(
                        progress = seekProgress,

                        onProgressChanged = {
                            seekProgress = it
                        },

                        onSeekFinished = {
                            player.seekTo(
                                (it * state.duration).toLong()
                            )
                            playerViewModel.seek((it* state.duration).toLong())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.9f)
                            .height(10.dp)
                            .background(
                                VMocha.Surface1, RoundedCornerShape(20.dp)
                            )
                    )

                    Text(
                        formatTime(state.duration),
                        modifier = Modifier.padding(start =5.dp, end = 5.dp),
                        color = VMocha.Text,
                        textAlign = TextAlign.End
                    )


                }
            }
        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {


            IconButton(
                //10sec <<
                onClick = {player.seekBackward()},
                // modifier = Modifier.align(Alignment.Center)
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
                onClick = { player.togglePlayPause()}
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
                onClick = {player.seekForward()},
                // modifier = Modifier.align(Alignment.Center)
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
}

