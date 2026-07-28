package com.example.showcase.features.Player.model

import com.example.showcase.features.InfoPage.uistate.MovieInfoUiModel
import com.example.showcase.features.InfoPage.uistate.SeriesInfoUiModel
import com.example.showcase.ui.theme.VMocha
import android.graphics.Color
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.CaptionStyleCompat


data class PlayerState(

    val media: PlayerMedia? = null,

    val playlist: List<PlayerMedia> = emptyList(),

    val currentIndex: Int = 0,

    val isPlaying: Boolean = false,

    val isBuffering: Boolean = false,

    val isEnded: Boolean = false,

    val currentPosition: Long = 0L,

    val duration: Long = 0L,

    val bufferedPosition: Long = 0L,

    val playbackSpeed: Float = 1f,

    val audioTracks: List<AudioTrack> = emptyList(),

    val subtitleTracks: List<SubtitleTrack> = emptyList(),

    val selectedAudioTrack: Int? = null,

    val selectedSubtitleTrack: Int? = null,

    val error: String? = null
)


data class SeriesPlaybackData(

    val playlist: List<PlayerMedia>,

    val startIndex: Int
)


data class AudioTrack(

    val groupIndex: Int,

    val trackIndex: Int,

    val language: String?,

    val label: String,

    val selected: Boolean
)

data class SubtitleTrack(

    val groupIndex: Int,

    val trackIndex: Int,

    val language: String?,

    val label: String,

    val selected: Boolean,

    val isOff: Boolean = false
)

@UnstableApi
data class SubtitleStyle(
    val textColor: Int = Color.WHITE, //font color
    val backgroundColor: Int = Color.BLACK, // background color
    val backgroundAlpha: Int = 160, // background opacity
    val textSize: Float = 18f, // font size
    val edgeType: Int = CaptionStyleCompat.EDGE_TYPE_NONE, //font edge
    val edgeColor: Int = Color.BLACK, // edge color
    val windowColor: Int = Color.TRANSPARENT // window color
)

enum class TrackSheetType {
    NONE,
    AUDIO,
    SUBTITLE
}

data class PlayerUiState(
    val sheet: TrackSheetType = TrackSheetType.NONE
)


data class PlayerInfoState(

    val loading: Boolean = false,

    val series: SeriesInfoUiModel? = null,

    val movie: MovieInfoUiModel? = null,

    val error: String? = null
)

data class PlaybackSpeed(
    val label: String,
    val speed: Float
)

val PlaybackSpeeds = listOf(
    PlaybackSpeed("0.25x", 0.25f),
    PlaybackSpeed("0.5x", 0.5f),
    PlaybackSpeed("0.75x", 0.75f),
    PlaybackSpeed("Normal", 1.0f),
    PlaybackSpeed("1.25x", 1.25f),
    PlaybackSpeed("1.5x", 1.5f),
    PlaybackSpeed("1.75x", 1.75f),
    PlaybackSpeed("2.0x", 2.0f)
)
