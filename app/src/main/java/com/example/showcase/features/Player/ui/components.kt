package com.example.showcase.features.Player.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.showcase.features.Library.ui.Components.TypewriterText
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.theme.VMocha

@Composable
fun SubtitleTrackSheet(
    vm: PlayerViewModel
) {

    val tracks by vm.subtitleTracks.collectAsState()

    LazyColumn {

        item {

            ListItem(
                headlineContent = {
                    Text("Off")
                },
                modifier =
                    Modifier.clickable {
                        vm.disableSubtitles()
                    }
            )
        }

        items(tracks) { track ->

            ListItem(

                headlineContent = {
                    Text(track.label)
                },

                supportingContent = {
                    Text(track.language.toString())
                },

                modifier =
                    Modifier.clickable {

                        vm.selectSubtitle(track)
                    }
            )
        }
    }
}

@Composable
fun AudioTrackSheet(
    vm: PlayerViewModel
) {

    val tracks by vm.audioTracks.collectAsState()

    LazyColumn {

        items(tracks) { track ->

            ListItem(

                headlineContent = {
                    Text(track.label)
                },

                supportingContent = {
                    Text(track.language.toString())
                },

                modifier =
                    Modifier.clickable {

                        vm.selectAudio(track)
                    }
            )
        }
    }
}

@Preview
@Composable
fun VisualizerBar() {

    val infiniteTransition = rememberInfiniteTransition()

    val height by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .width(6.dp)
            .height(height.dp)
            .background(Color.Green, RoundedCornerShape(50))
    )
}


@Composable
fun MedialPlean(
    playerInfoState: PlayerInfoState,
    state: PlayerState,
    playerViewModel: PlayerViewModel
) {
    val state by PlayerGraph.playerManager.playerState.collectAsState()
    val player = PlayerGraph.playerManager


    val progress =

        if (state.duration > 0)

            state.currentPosition.toFloat() /
                    state.duration

        else

            0f

    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 5.dp)
            .background(
                VMocha.Crust, RoundedCornerShape(20.dp)
            )
    ) {
        Column() {
            Box(Modifier.fillMaxWidth()) {
                SeekBar(
                    progress, {}, {},
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(10.dp)
                        .align(Alignment.CenterStart)
                )
                Row(
                    Modifier.align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {playerViewModel.showSubtitleSheet()
                        }
                    ) {
                        Icon(
                            Icons.Default.Subtitles,
                            contentDescription = null,
                            tint = VMocha.Text
                        )
                    }

                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {playerViewModel.showAudioSheet()
                        },
                    ) {
                        Icon(
                            Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = VMocha.Text
                        )
                    }
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {playerViewModel.toggleFullscreen()
                        },
                    ) {
                        Icon(
                            Icons.Default.FitScreen,
                            contentDescription = null,
                            tint = VMocha.Text
                        )
                    }


                }
            }

            Row(
                Modifier.fillMaxSize()
            ) {
                if (state.media?.type == MediaType.EPISODE) {
                    AsyncImage(
                        model = playerInfoState.series?.media?.poster?.uri,
                        contentDescription = "",
                        Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    AsyncImage(
                        model = state.media?.poster,
                        contentDescription = "",
                        Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }

                Column(Modifier.padding(5.dp)) {
                    MidenControls(playerViewModel,player,state)
                    Divider(Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp), color = VMocha.Subtext0)
                    if (state.media?.type == MediaType.EPISODE) {
                        LazyColumn() {
                            item {
                                TypewriterText(
                                    playerInfoState.series?.metadata?.title ?: " ",//title
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VMocha.Text
                                )
                                TypewriterText(
                                    playerInfoState.series?.metadata?.originalTitle ?: " ",//OGtitle
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VMocha.Text
                                )
                            }
                            item {
                                Text(
                                    playerInfoState.series?.metadata?.plot ?: " ",
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Start,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    color = VMocha.Subtext0
                                )
                                Spacer(Modifier.padding(5.dp))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = playerInfoState.series?.metadata?.rating.toString() + "|" + playerInfoState.series?.metadata?.year.toString(),//Rating,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.End,
                                    overflow = TextOverflow.Ellipsis,
                                    color = VMocha.Subtext0
                                )
                            }
                        }
                    } else {

                        LazyColumn() {
                            item {
                                TypewriterText(
                                    playerInfoState.movie?.metadata?.title ?: " ",//title
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VMocha.Text
                                )
                                TypewriterText(
                                    playerInfoState.movie?.metadata?.originalTitle ?: " ",//OGtitle
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VMocha.Text
                                )
                            }
                            item {
                                Text(
                                    playerInfoState.movie?.metadata?.plot ?: " ",
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Start,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    color = VMocha.Subtext0
                                )
                                Spacer(Modifier.padding(5.dp))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = playerInfoState.movie?.metadata?.rating.toString() + " | " + playerInfoState.movie?.metadata?.year.toString(),//Rating,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.End,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    color = VMocha.Subtext0
                                )
                            }

                        }
                    }


                }

            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MidenControls(playerViewModel: PlayerViewModel, player: PlayerManager, state: PlayerState) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                //10sec <<
                onClick = {player.seekBackward()
                },
                // modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    Icons.Default.KeyboardDoubleArrowLeft,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }


            IconButton(
                //last ep
                onClick = {playerViewModel.playPreviousEpisode()
                },
                // modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }

            IconButton(
                //play pause
                onClick = { player.togglePlayPause()
                }
            ) {
                Icon(
                    imageVector =
                    if (state.isPlaying){
                        Icons.Default.PauseCircle}
                    else{
                        Icons.Default.PlayCircle},
                    contentDescription = null,
                    tint = VMocha.Text,
                    modifier = Modifier.size(150.dp)

                )
            }

            IconButton(
                //next EP
                onClick = {playerViewModel.playNextEpisode()
                },
            ) {
                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }

            IconButton(
                //10sec >>
                onClick = {player.seekForward()
                },
                // modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    Icons.Default.KeyboardDoubleArrowRight,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }
        }
    }
}
