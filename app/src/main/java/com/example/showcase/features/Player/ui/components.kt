package com.example.showcase.features.Player.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.ScreenLockRotation
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.features.Library.ui.Components.TypewriterText
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerEngine
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.theme.VMocha
import android.content.pm.ActivityInfo
import android.widget.ToggleButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext

@Composable
fun SubtitleTrackSheet(
	vm: PlayerViewModel
) {

	val tracks by vm.subtitleTracks.collectAsState()

	LazyColumn {

		items(
			items = tracks,
			key = { track ->
				"${track.groupIndex}_${track.trackIndex}_${track.isOff}"
			}
		) { track ->

			ListItem(

				headlineContent = {
					Text(track.label)
				},

				supportingContent = {

					if (!track.isOff) {
						Text(
							track.language ?: "Unknown language"
						)
					}
				},

				modifier =
					Modifier.clickable {

						if (track.isOff) {
							vm.disableSubtitles()
						} else {
							vm.selectSubtitle(track)
						}
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

		items(
			items = tracks,
			key = { track ->
				"${track.groupIndex}_${track.trackIndex}"
			}
		) { track ->

			ListItem(

				headlineContent = {
					Text(track.label)
				},

				supportingContent = {
					Text(
						track.language ?: "Unknown language"
					)
				},

				modifier =
					Modifier.clickable {

						vm.selectAudio(
							track
						)
					}
			)
		}
	}
}


@Composable
fun MedialPlean(
	playerInfoState: PlayerInfoState,
	playerViewModel: PlayerViewModel,
	playerEngine: PlayerEngine= PlayerEngine.VLC,
) {
	val exoState by PlayerGraph.playerManager.playerState.collectAsState()
	val vlcState by PlayerGraph.vlcPlayerManager.playerState.collectAsState()

	val state = when (playerEngine) {
		PlayerEngine.EXO -> exoState
		PlayerEngine.VLC -> vlcState
	}
	val player = PlayerGraph.playerManager


	val progress = if (state.duration > 0L) {
		state.currentPosition.toFloat() / state.duration.toFloat()
	} else {
		0f
	}

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
						onClick = {
							playerViewModel.showSubtitleSheet()
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
						onClick = {
							playerViewModel.showAudioSheet()
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
						onClick = {
							playerViewModel.toggleFullscreen()
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
					MidenControls(playerViewModel, player, state,playerEngine)
					Divider(
						Modifier
							.fillMaxWidth()
							.padding(bottom = 2.dp), color = VMocha.Subtext0
					)
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
									maxLines = 5,
									style = TextStyle(
										lineHeight = 12.sp
									),
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
									color = VMocha.Subtext0,
									style = TextStyle(
										lineHeight = 12.sp
									),
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
									maxLines = 5,
									overflow = TextOverflow.Ellipsis,
									color = VMocha.Subtext0,
									style = TextStyle(
										lineHeight = 12.sp
									),
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


@Composable
fun MidenControls(
	playerViewModel: PlayerViewModel,
	player: PlayerManager,
	state: PlayerState,
	playerEngine: PlayerEngine
) {

	Column(modifier = Modifier.fillMaxWidth()) {
		Row(
			Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {

			IconButton(
				//10sec <<
				onClick = {
					when (playerEngine) {
						PlayerEngine.EXO -> PlayerGraph.playerManager.seekBackward()
						PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.seekBackward()
					}
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
				onClick = {
					playerViewModel.playPreviousEpisode()
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
				onClick = {
					when (playerEngine) {
						PlayerEngine.EXO -> PlayerGraph.playerManager.togglePlayPause()
						PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.togglePlayPause()
					}
				}
			) {
				Icon(
					imageVector =
						if (state.isPlaying) {
							Icons.Default.PauseCircle
						} else {
							Icons.Default.PlayCircle
						},
					contentDescription = null,
					tint = VMocha.Text,
					modifier = Modifier.size(150.dp)

				)
			}

			IconButton(
				//next EP
				onClick = {
					playerViewModel.playNextEpisode()
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
				onClick = {
					when (playerEngine) {
						PlayerEngine.EXO -> PlayerGraph.playerManager.seekForward()
						PlayerEngine.VLC -> PlayerGraph.vlcPlayerManager.seekForward()
					}
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerSettings() {

	Dialog({}) {
		Card(
			Modifier
				.size(500.dp)
				.background(VMocha.Crust, RoundedCornerShape(20.dp))
		)
		{
			LazyColumn(
				Modifier
					.fillMaxSize()
					.background(VMocha.Crust, RoundedCornerShape(20.dp))
			) {
				item {
					CaptionSetting()
				}
			}
		}
	}

}

enum class RotationMode {
	AUTO,
	LANDSCAPE,
	PORTRAIT
}
fun Context.findActivity(): Activity? {
	var context = this
	while (context is ContextWrapper) {
		if (context is Activity) return context
		context = context.baseContext
	}
	return null
}
@Composable
fun AutoRotationToggleButton(
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current
	val activity = remember(context) { context.findActivity() }

	// Track rotation state
	var currentMode by remember { mutableIntStateOf(RotationMode.AUTO.ordinal) }

	IconButton(
		modifier = modifier,
		onClick = {
			activity?.let { act ->
				when (currentMode) {
					RotationMode.AUTO.ordinal -> {
						// Switch to Locked Landscape
						act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
						currentMode = RotationMode.LANDSCAPE.ordinal
					}
					RotationMode.LANDSCAPE.ordinal -> {
						// Switch to Locked Portrait
						act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
						currentMode = RotationMode.PORTRAIT.ordinal
					}
					else -> {
						// Back to System Auto-Rotate
						act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
						currentMode = RotationMode.AUTO.ordinal
					}
				}
			}
		}
	) {
		val icon = if (currentMode == RotationMode.AUTO.ordinal) {
			Icons.Default.ScreenRotation
		} else {
			Icons.Default.ScreenLockRotation
		}

		Icon(
			imageVector = icon,
			contentDescription = "Toggle Rotation Lock"
		)
	}
}

@Preview
@Composable
private fun CaptionSetting() {
	Column() {
		Text(
			"Captions Setting",
			fontSize = 20.sp,
			color = VMocha.Text,
			textAlign = TextAlign.Start,
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
		)
		Row(verticalAlignment = Alignment.CenterVertically) {
            var size by remember {
                mutableStateOf(1)
            }


			Text(
				"Size",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)
			Row(
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
			) {
				IconButton({
					if (size < 20)size += 1
                }, modifier = Modifier.size(20.dp)) {
					Icon(
					 Icons.Default.ExposurePlus1,
						contentDescription = "",
						tint = VMocha.Text
					)
				}
                Spacer(Modifier.size(5.dp))
				Text(
					size.toString(),
					fontSize = 15.sp,
					color = VMocha.Text,
					textAlign = TextAlign.Start,
					modifier = Modifier
						.padding(10.dp)
				)
                Spacer(Modifier.size(5.dp))
                IconButton({
					if (size > 0) size -= 1
                }, modifier = Modifier.size(20.dp)) {
                    Icon(
                        Icons.Default.ExposureNeg1,
                        contentDescription = "",
                        tint = VMocha.Text
                    )
                }
				Spacer(Modifier.size(5.dp))
			}
		}
		Row(verticalAlignment = Alignment.CenterVertically) {
			Text(
				"Text Color",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {

				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Text
					)
				}

				Spacer(Modifier.size(5.dp))

				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Mantle
					)
				}
				Spacer(Modifier.size(5.dp))
				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Red
					)
				}
				Spacer(Modifier.size(5.dp))
				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Lavender
					)
				}
				Spacer(Modifier.size(5.dp))
				IconButton(
					{}, modifier = Modifier
						.size(20.dp)
						.drawBehind {
							drawCircle(
								brush = Brush.radialGradient(
									colors = listOf(
										VMocha.Blue.copy(0.6f),
										VMocha.Blue.copy(0.2f),
										Color.Transparent
									)
								),
								radius = size.maxDimension / 1f
							)
						}) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Blue
					)
				}
				Spacer(Modifier.size(5.dp))
			}

		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				"BackGround",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {
				Switch(
					checked = true,
					onCheckedChange = {},
					colors = SwitchDefaults.colors(checkedTrackColor = VMocha.Blue, uncheckedTrackColor = VMocha.Text),
					modifier = Modifier.scale(0.8f)
				)
			}
		}


		Row(verticalAlignment = Alignment.CenterVertically) {
			Text(
				"BackGround Color",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {

				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Text
					)
				}

				Spacer(Modifier.size(5.dp))

				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Mantle
					)
				}
				Spacer(Modifier.size(5.dp))
				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Red
					)
				}
				Spacer(Modifier.size(5.dp))
				IconButton({}, modifier = Modifier.size(20.dp)) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Lavender
					)
				}
				Spacer(Modifier.size(5.dp))
				IconButton(
					{}, modifier = Modifier
						.size(20.dp)
						.drawBehind {
							drawCircle(
								brush = Brush.radialGradient(
									colors = listOf(
										VMocha.Blue.copy(0.6f),
										VMocha.Blue.copy(0.2f),
										Color.Transparent
									)
								),
								radius = size.maxDimension / 1f
							)
						}) {
					Icon(
						painter = painterResource(R.drawable.newmoon),
						contentDescription = "",
						tint = VMocha.Blue
					)
				}
				Spacer(Modifier.size(5.dp))

			}

		}
	}

}