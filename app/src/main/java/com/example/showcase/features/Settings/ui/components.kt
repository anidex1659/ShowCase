package com.example.showcase.features.Settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.xr.compose.subspace.layout.SpatialAlignment
import com.example.showcase.R
import com.example.showcase.core.SettingesManger.SettingsManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerEngine
import com.example.showcase.features.Player.ui.VideoPlayer
import com.example.showcase.ui.theme.Themes
import com.example.showcase.ui.theme.VMocha

@Preview
@Composable
fun ThemesSettings() {
	val settings by SettingsManager.settings
		.collectAsStateWithLifecycle()
	val items = listOf(
		"Catppuccin",
		"AmoledBlack",
		"CatppuccinAMOLED",
		"CatppuccinLatte",
		"RosePine",
		"RosePineMoon",
		"RosePineDawn",
		"TokyoNight",
	)
	var expanded by remember { mutableStateOf(false) }
	var selectedText by remember { mutableStateOf("Catppuccin") }
	Column() {
		Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
			Text(
				"Theme",
				fontSize = 20.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)
			Box(
				Modifier
					.fillMaxWidth()
			) {
				Box(
					modifier = Modifier
						.align(Alignment.CenterEnd)
						.wrapContentSize(Alignment.TopStart)
				) {
					Button(
						shape = RoundedCornerShape(20.dp),
						onClick = { expanded = true },
						colors = ButtonDefaults.textButtonColors(
							VMocha.Mantle, contentColor = VMocha.Text
						), modifier = Modifier
							.fillMaxWidth(0.5f)
							.height(35.dp)
							.align(Alignment.CenterEnd)
					)
					{
						Text(text = settings.colorPalate)
					}
					DropdownMenu(
						expanded = expanded,
						onDismissRequest = { expanded = false }
					) {
						items.forEach { item ->
							DropdownMenuItem(
								text = { Text(text = item) },
								onClick = {
									selectedText = item
									when (selectedText) {

										"Catppuccin" ->
											SettingsManager.setColorPalette(Themes.Catppuccin)

										"AmoledBlack" ->
											SettingsManager.setColorPalette(Themes.AmoledBlack)

										"CatppuccinAMOLED" ->
											SettingsManager.setColorPalette(Themes.CatppuccinAMOLED)

										"CatppuccinLatte" ->
											SettingsManager.setColorPalette(Themes.CatppuccinLatte)

										"RosePine" ->
											SettingsManager.setColorPalette(Themes.RosePine)

										"RosePineMoon" ->
											SettingsManager.setColorPalette(Themes.RosePineMoon)

										"RosePineDawn" ->
											SettingsManager.setColorPalette(Themes.RosePineDawn)

										"TokyoNight" ->
											SettingsManager.setColorPalette(Themes.TokyoNight)


										else ->
											SettingsManager.setColorPalette(Themes.Catppuccin)

									}
									expanded = false
								}
							)
						}
					}
				}
			}

		}
		Box(
			Modifier
				.fillMaxWidth()
				.padding(vertical = 10.dp)
		) {
			Box(
				Modifier
					.align(Alignment.Center)
					.fillMaxWidth(0.8f)
					.height(20.dp)
					.border(
						1.dp,
						VMocha.Text,
						shape = RoundedCornerShape(20.dp))
					.background(
						brush = Brush.linearGradient(
							colors = listOf(
								VMocha.Blue,
								VMocha.Red,
								VMocha.Lavender,
								VMocha.Text,
								VMocha.Rosewater,
								VMocha.Mantle,
								VMocha.Crust
							)
						), shape = RoundedCornerShape(20.dp)
					)
			)
		}

	}
}


@UnstableApi
@Preview
@Composable
fun Playback() {
	val settings by SettingsManager.settings
		.collectAsStateWithLifecycle()
	Column() {
		Text(
			"PlayBack",
			fontSize = 20.sp,
			color = VMocha.Text,
			textAlign = TextAlign.Start,
			modifier = Modifier
				.padding(10.dp)
		)
		Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
			Text(
				"PlayerEngin",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)
			Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
				IconButton({ SettingsManager.setPlayerEngin(PlayerEngine.EXO)}) {
					Icon(
						painter = painterResource(R.drawable.hardware),
						"",
						tint = if (settings.playerEngine=="EXO") VMocha.Blue else VMocha.Subtext0,
						modifier = Modifier.scale(0.8f)
					)
				}
				IconButton({SettingsManager.setPlayerEngin(PlayerEngine.VLC)}) {
					Icon(
						painter = painterResource(R.drawable.software),
						"",
						tint = if (settings.playerEngine=="VLC") VMocha.Blue else VMocha.Subtext0,
						modifier = Modifier.scale(0.8f)
					)
				}
			}
		}

		Row (Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
			Text(
				"Seek For/Backward",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)
			Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
				IconButton({
					SettingsManager.setSeekAmount(((settings.seekAmount)-10_000))
				}) {
					Icon(
						Icons.Default.ExposureNeg1, "", tint = VMocha.Text
					)
				}
				Text(
					(settings.seekAmount/1_000).toString(),
					fontSize = 15.sp,
					color = VMocha.Text,
					textAlign = TextAlign.Start,
					modifier = Modifier
						.padding(10.dp)
				)

				IconButton({SettingsManager.setSeekAmount((settings.seekAmount)+10_000)}) {
					Icon(
						Icons.Default.ExposurePlus1, "",tint = VMocha.Text
					)
				}

			}
		}

		Row (Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
			Text(
				"Auto Next Ep",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier
					.padding(10.dp)
			)
			Box( modifier = Modifier.fillMaxWidth()) {
				Switch(
					checked = settings.autoPlayNextEpisode,
					onCheckedChange = { enabled ->
						SettingsManager.setAutoPlayNextEpisode(
							if (enabled) false else true
						)
					},
					colors = SwitchDefaults.colors(
						checkedTrackColor = VMocha.Blue,
						uncheckedTrackColor = VMocha.Text
					),
					modifier = Modifier
						.height(10.dp)
						.scale(0.8f)
						.align(Alignment.CenterEnd)
				)
			}

		}
	}
}

@UnstableApi
@Composable
fun CaptionSetting() {

	val settings by SettingsManager.settings
		.collectAsStateWithLifecycle()

	Column {

		Row(
			modifier = Modifier.fillMaxWidth()
		) {

			Text(
				text = "Captions",
				fontSize = 20.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier.padding(10.dp)
			)

			Box {

				Text(
					text = "Aa Bb Cc",

					// Live font size
					fontSize = settings.textSize.sp,

					// Live text color
					color = Color(settings.textColor),

					// Live background color
					style = TextStyle(
						background = Color(
							settings.backgroundColor
						).copy(
							alpha = settings.backgroundAlpha / 255f
						)
					),

					textAlign = TextAlign.Start,

					modifier = Modifier
						.fillMaxWidth()
						.padding(10.dp)
				)
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically
		) {

			Text(
				text = "Size",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier.padding(10.dp)
			)

			Row(
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {

				Slider(

					// Read from SettingsManager
					value = settings.textSize,

					// Save directly to SettingsManager
					onValueChange = { newSize ->

						SettingsManager.setCaptionTextSize(
							newSize.sp
						)
					},

					valueRange = 8f..28f,

					steps = 19,

					colors = SliderDefaults.colors(
						thumbColor = VMocha.Blue,
						activeTrackColor = VMocha.Blue,
						inactiveTrackColor = VMocha.Text,
						activeTickColor = VMocha.Text,
						inactiveTickColor = VMocha.Blue
					),

					modifier = Modifier
						.fillMaxWidth(0.35f)
						.height(20.dp)
				)
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically
		) {

			Text(
				text = "Background",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier.padding(10.dp)
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {

				Switch(

					// Background is enabled when alpha > 0
					checked = settings.backgroundAlpha > 0,

					onCheckedChange = { enabled ->

						SettingsManager
							.setCaptionBackgroundAlpha(

								if (enabled) {
									160
								} else {
									0
								}
							)
					},

					colors = SwitchDefaults.colors(
						checkedTrackColor = VMocha.Blue,
						uncheckedTrackColor = VMocha.Text
					),

					modifier = Modifier
						.height(10.dp)
						.scale(0.8f)
				)
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically
		) {

			Text(
				text = "Text Color",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier.padding(10.dp)
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {

				// White
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionTextColor(
								android.graphics.Color.WHITE
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "White",
						tint = Color.White
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Mantle
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionTextColor(
								VMocha.Mantle
									.toArgb()
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Mantle",
						tint = VMocha.Mantle
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Red
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionTextColor(
								VMocha.Red
									.toArgb()
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Red",
						tint = VMocha.Red
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Lavender
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionTextColor(
								VMocha.Lavender
									.toArgb()
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Lavender",
						tint = VMocha.Lavender
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Blue
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionTextColor(
								VMocha.Blue
									.toArgb()
							)
					},

					modifier = Modifier
						.size(20.dp)
						.drawBehind {

							drawCircle(
								brush = Brush.radialGradient(
									colors = listOf(
										VMocha.Blue.copy(
											alpha = 0.6f
										), VMocha.Blue.copy(
											alpha = 0.2f
										), Color.Transparent
									)
								),

								radius = size.maxDimension / 1f
							)
						}
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Blue",
						tint = VMocha.Blue
					)
				}
			}
		}
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {

			Text(
				text = "Background Color",
				fontSize = 15.sp,
				color = VMocha.Text,
				textAlign = TextAlign.Start,
				modifier = Modifier.padding(10.dp)
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {

				// Black
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionBackgroundColor(
								android.graphics.Color.BLACK
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Black",
						tint = Color.Black
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Mantle
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionBackgroundColor(
								VMocha.Mantle.toArgb()
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Mantle",
						tint = VMocha.Mantle
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Red
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionBackgroundColor(
								VMocha.Red.toArgb()
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Red",
						tint = VMocha.Red
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Lavender
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionBackgroundColor(
								VMocha.Lavender.toArgb()
							)
					},
					modifier = Modifier.size(20.dp)
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Lavender",
						tint = VMocha.Lavender
					)
				}


				Spacer(
					Modifier.size(5.dp)
				)


				// Blue
				IconButton(
					onClick = {

						SettingsManager
							.setCaptionBackgroundColor(
								VMocha.Blue.toArgb()
							)
					},
					modifier = Modifier
						.size(20.dp)
						.drawBehind {

							drawCircle(
								brush = Brush.radialGradient(
									colors = listOf(
										VMocha.Blue.copy(
											alpha = 0.6f
										), VMocha.Blue.copy(
											alpha = 0.2f
										), Color.Transparent
									)
								),

								radius = size.maxDimension / 1f
							)
						}
				) {

					Icon(
						painter = painterResource(
							R.drawable.newmoon
						),
						contentDescription = "Blue",
						tint = VMocha.Blue
					)
				}
			}
		}
	}
}