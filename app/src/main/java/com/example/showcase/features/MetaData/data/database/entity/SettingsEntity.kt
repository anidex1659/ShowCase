package com.example.showcase.features.MetaData.data.database.entity

import android.graphics.Color
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.CaptionStyleCompat
import androidx.room.Entity
import androidx.room.PrimaryKey

@UnstableApi
@Entity(tableName = "settings")
data class SettingsEntity(

	@PrimaryKey
	val id: Int = 1,

	// UI
	val colorPalate : String="Catppuccin",

	// TMDB
	val useCustomTmdbKey: Boolean = false,
	val apiValidated: Boolean=false,
	val customTmdbApiKey: String = "",

	// Playback
	val seekAmount : Long =10_000L,
	val playerEngine: String ="EXO",
	val autoPlayNextEpisode: Boolean = true,
	val rememberPlaybackPosition: Boolean = true,

	// Caption
	val textColor: Int = Color.WHITE, //font color
	val backgroundColor: Int = Color.BLACK, // background color
	val backgroundAlpha: Int = 160, // background opacity
	val textSize: Float = 18f, // font size
	val edgeType: Int = CaptionStyleCompat.EDGE_TYPE_NONE, //font edge
	val edgeColor: Int = Color.BLACK, // edge color
	val windowColor: Int = Color.TRANSPARENT // window color


)