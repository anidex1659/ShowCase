package com.example.showcase.features.Settings.ui

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.showcase.core.SettingesManger.SettingsManager
import com.example.showcase.features.Library.ui.Components.TypewriterText
import com.example.showcase.ui.Navigation.HomeRoute
import com.example.showcase.ui.Theme.VMocha

@OptIn(UnstableApi::class)
@Composable
fun Settings(navController: NavHostController) {
	var start by remember { mutableStateOf(false) }
	var showTmdbDilog by remember { mutableStateOf(false)  }
	val hight by animateDpAsState(
		targetValue = if (start) 250.dp else 0.dp,
		tween  (1000)
	)
	LaunchedEffect(start) {
		start=true
	}
	var apiKey by rememberSaveable {
		mutableStateOf(SettingsManager.tmdbApi)
	}

	val contex =LocalContext.current
	val uriHandler = LocalUriHandler.current

	if (showTmdbDilog) TmdbApiKeyDialog(
		apiKey = apiKey,

		apiAvailable = SettingsManager.tmdbApiAvailable,

		apiValidated = SettingsManager.apiValidated,

		onApiKeyChange = { apiKey = it },

		onOpenTmdb = { uriHandler.openUri("https://www.themoviedb.org/") },

		onDismiss = {showTmdbDilog=false},

		onSave = {
			SettingsManager.set_tmdbApi(
				apiKey.trim()
			)
			SettingsManager.set_tmdbApiAvailable(
				true
			)
			SettingsManager.set_tmdbApiValidaty(
				false
			)
			showTmdbDilog=false
			Toast.makeText(contex,"Custom Tm-db API Updated", Toast.LENGTH_SHORT).show()
		})

	Column(
		Modifier
			.fillMaxSize()
			.background(VMocha.Crust)
	)
	{
		Box(
			Modifier
				.padding(3.dp)
				.fillMaxWidth()
				.height(hight)
				.background(
					VMocha.Mantle,
					shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
				)
		) {
			TypewriterText(
				"ShowCase",
				color = VMocha.Text,
				style = TextStyle(
					fontFamily = FontFamily.Serif
				),
				fontSize = 30.sp,
				fontWeight = FontWeight.ExtraBold,
				textAlign = TextAlign.Center,
				modifier = Modifier.align(Alignment.Center)
			)
			Text(
				"ver 1",
				color = VMocha.Subtext1.copy(0.5f),
				fontSize = 12.sp,
				modifier = Modifier.align(Alignment.TopEnd)
			)
			Text(
				"Setting Take Affect After Relaunch",
				color = VMocha.Subtext1.copy(0.5f),
				fontSize = 8.sp,
				modifier = Modifier.align(Alignment.BottomEnd)
			)
			IconButton(onClick = {
				navController.navigate(HomeRoute)
			}, modifier = Modifier.align(Alignment.BottomStart)) {
				Icon(
					Icons.Default.ArrowBack,
					"",
					tint = VMocha.Text
				)
			}
		}
		LazyColumn (Modifier.fillMaxWidth()){

			item {
				Spacer(Modifier.height(20.dp))
				ThemesSettings()
			}
			item {
				Divider(Modifier.padding(5.dp), color = VMocha.Subtext0)
				Playback()
			}
			item {
				Divider(Modifier.padding(5.dp), color = VMocha.Subtext0)
				CaptionSetting()
			}
			item {
				Divider(Modifier.padding(5.dp), color = VMocha.Subtext0)
				TmdbApi(onEdit = {
					showTmdbDilog = true
				})
			}
		}
	}
}