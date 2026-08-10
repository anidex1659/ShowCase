package com.example.showcase.core.SettingesManger

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.xr.arcore.VpsAvailabilityAvailable
import com.example.showcase.features.MetaData.data.database.dao.SettingsDao
import com.example.showcase.features.MetaData.data.database.entity.SettingsEntity
import com.example.showcase.features.Player.PlayerViewModel.PlayerEngine
import com.example.showcase.ui.Theme.ShowcasePalette
import com.example.showcase.ui.Theme.Themes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@UnstableApi
object SettingsManager {

	private lateinit var dao: SettingsDao

	private val scope =
		CoroutineScope(
			SupervisorJob() + Dispatchers.IO
		)

	private val _settings =
		MutableStateFlow(
			SettingsEntity()
		)

	val settings: StateFlow<SettingsEntity>
		get() = _settings.asStateFlow()


	//Tm-dbApi
	val tmdbApiAvailable: Boolean
		get() = _settings.value.useCustomTmdbKey

	val apiValidated: Boolean
		get() = _settings.value.apiValidated

	val tmdbApi: String
		get() = _settings.value.customTmdbApiKey

	fun set_tmdbApi (
		api: String
	)
	{
		updateSettings {
			copy(
				customTmdbApiKey = api
			)
		}
	}

	fun set_tmdbApiValidaty (
		valid: Boolean
	)
	{
		updateSettings {
			copy(
				apiValidated=valid
			)
		}
	}

	fun set_tmdbApiAvailable (
		available: Boolean
	)
	{
		updateSettings {
			copy(
				useCustomTmdbKey = available
			)
		}
	}



	// UI / APPEARANCE
	val colorPalette: ShowcasePalette
		get() = when (_settings.value.colorPalate) {

			"Catppuccin" ->
				Themes.Catppuccin

			"AmoledBlack" ->
				Themes.AmoledBlack

			"CatppuccinAMOLED" ->
				Themes.CatppuccinAMOLED

			"CatppuccinLatte" ->
				Themes.CatppuccinLatte

			"RosePine" ->
				Themes.RosePine

			"RosePineMoon" ->
				Themes.RosePineMoon

			"RosePineDawn" ->
				Themes.RosePineDawn

			"TokyoNight" ->
				Themes.TokyoNight


			else ->
				Themes.Catppuccin
		}


	/**
	 * Saves the selected ShowcasePalette
	 * as a String in Room.
	 */
	fun setColorPalette(
		palette: ShowcasePalette
	) {

		val paletteName =
			when (palette) {

				Themes.Catppuccin ->
					"Catppuccin"

				Themes.AmoledBlack ->
					"AmoledBlack"

				Themes.CatppuccinLatte ->
					"CatppuccinLatte"

				Themes.CatppuccinAMOLED ->
					"CatppuccinAMOLED"

				Themes.RosePine ->
					"RosePine"

				Themes.RosePineDawn ->
					"RosePineDawn"

				Themes.RosePineMoon ->
					"RosePineMoon"

				Themes.TokyoNight ->
					"TokyoNight"

				else ->
					"Catppuccin"
			}

		updateSettings {

			copy(
				colorPalate = paletteName
			)
		}
	}


	// =========================================================
	// PLAYBACK
	// =========================================================

	val seekAmount : Long
		get()=_settings.value.seekAmount

	fun setSeekAmount(amount : Long){
		updateSettings {
			copy(
				seekAmount = amount
			)
		}
	}

	val playerEngine: PlayerEngine
		get() = when(_settings.value.playerEngine){
			"EXO"->
				PlayerEngine.EXO
			"VLC"->
				PlayerEngine.VLC
			else ->
				PlayerEngine.EXO
		}

	fun setPlayerEngin(playerEngine: PlayerEngine) {
		val engin: String =
			when (playerEngine) {
				PlayerEngine.EXO ->
					"EXO"

				PlayerEngine.VLC ->
					"VLC"

				else ->
					"EXO"
			}
		updateSettings {
			copy(playerEngine = engin)
		}
	}

	val autoPlayNextEpisode: Boolean
		get() =
			_settings.value.autoPlayNextEpisode


	fun setAutoPlayNextEpisode(
		enabled: Boolean
	) {

		updateSettings {

			copy(
				autoPlayNextEpisode = enabled
			)
		}
	}


	val rememberPlaybackPosition: Boolean
		get() =
			_settings.value.rememberPlaybackPosition


	fun setRememberPlaybackPosition(
		enabled: Boolean
	) {

		updateSettings {

			copy(
				rememberPlaybackPosition = enabled
			)
		}
	}


	// =========================================================
	// CAPTION / SUBTITLE SETTINGS
	// =========================================================

	val captionTextColor: Int
		get() =
			_settings.value.textColor


	fun setCaptionTextColor(
		color: Int
	) {

		updateSettings {

			copy(
				textColor = color
			)
		}
	}


	val captionBackgroundColor: Int
		get() =
			_settings.value.backgroundColor


	fun setCaptionBackgroundColor(
		color: Int
	) {

		updateSettings {

			copy(
				backgroundColor = color
			)
		}
	}


	val captionBackgroundAlpha: Int
		get() =
			_settings.value.backgroundAlpha


	fun setCaptionBackgroundAlpha(
		alpha: Int
	) {

		updateSettings {

			copy(
				backgroundAlpha = alpha
			)
		}
	}


	val captionTextSize: TextUnit
		get() =
			_settings.value.textSize.sp


	fun setCaptionTextSize(
		size: TextUnit
	) {

		updateSettings {

			copy(
				textSize = size.value
			)
		}
	}


	val captionEdgeType: Int
		get() =
			_settings.value.edgeType


	fun setCaptionEdgeType(
		edgeType: Int
	) {

		updateSettings {

			copy(
				edgeType = edgeType
			)
		}
	}


	val captionEdgeColor: Int
		get() =
			_settings.value.edgeColor


	fun setCaptionEdgeColor(
		color: Int
	) {

		updateSettings {

			copy(
				edgeColor = color
			)
		}
	}


	val captionWindowColor: Int
		get() =
			_settings.value.windowColor


	fun setCaptionWindowColor(
		color: Int
	) {

		updateSettings {

			copy(
				windowColor = color
			)
		}
	}


	// =========================================================
	// INITIALIZATION
	// =========================================================

	fun initialize(
		settingsDao: SettingsDao
	) {

		dao =
			settingsDao

		scope.launch {

			val savedSettings =
				dao.getSettings()

			if (savedSettings == null) {

				val defaultSettings =
					SettingsEntity()

				dao.saveSettings(
					defaultSettings
				)

				_settings.value =
					defaultSettings

			} else {

				_settings.value =
					savedSettings
			}
		}
	}


	// =========================================================
	// INTERNAL UPDATE
	// =========================================================

	private fun updateSettings(
		update: SettingsEntity.() -> SettingsEntity
	) {

		val newSettings =
			_settings.value.update()

		// Update StateFlow immediately
		_settings.value =
			newSettings

		// Persist to Room
		scope.launch {

			dao.saveSettings(
				newSettings
			)
		}
	}
}