package com.example.showcase.features.Player.PlayerCore

import android.content.Context
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.exoplayer.ExoPlayer
import com.example.showcase.features.Player.model.AudioTrack
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.features.Player.model.SubtitleTrack
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerManager(
    context: Context
) {

    companion object {

        private const val UPDATE_INTERVAL = 500L

        private const val SEEK_AMOUNT = 10_000L

    }

    val player = ExoPlayer.Builder(context).build()

    private val scope =
        CoroutineScope(
            Dispatchers.Main +
                    SupervisorJob()
        )

    private var progressJob: Job? = null

    private val _playerState =
        MutableStateFlow(PlayerState())

    val playerState: StateFlow<PlayerState> =
        _playerState.asStateFlow()


    var onPlaybackEnded: (() -> Unit)? = null  // notify view model on episode finish to play next ep
    var onProgress: ((PlayerState) -> Unit)? = null // notify view model to store episode playback

    enum class VideoResizeMode {
        FIT,
        FILL,
        ZOOM,
        STRETCH
    }


    init {

        player.addListener(

            object : Player.Listener {

                override fun onIsPlayingChanged(isPlaying: Boolean) {

                    _playerState.update {

                        it.copy(
                            isPlaying = isPlaying
                        )

                    }

                    if (isPlaying)
                        startProgressUpdates()
                }

                override fun onPlaybackStateChanged(state: Int) {

                    when (state) {

                        Player.STATE_IDLE -> {

                            _playerState.update {

                                it.copy(

                                    isBuffering = false,

                                    isEnded = false
                                )

                            }
                        }

                        Player.STATE_BUFFERING -> {

                            _playerState.update {

                                it.copy(
                                    isBuffering = true
                                )

                            }
                        }

                        Player.STATE_READY -> {

                            _playerState.update {

                                it.copy(

                                    isBuffering = false,

                                    duration = player.duration.coerceAtLeast(0L),

                                )

                            }
                            updateTracks()
                        }

                        Player.STATE_ENDED -> {

                            _playerState.update {

                                it.copy(
                                    isPlaying = false,
                                    isEnded = true,
                                    currentPosition = player.duration,
                                    duration = player.duration
                                )
                            }

                            onProgress?.invoke(_playerState.value)
                            Log.d("Anidex_PLAYER", "ENDED")
                            onPlaybackEnded?.invoke()
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {

                    _playerState.update {

                        it.copy(
                            error = error.localizedMessage
                        )

                    }
                }
            }
        )
    }

    private val _resizeMode =
        MutableStateFlow(VideoResizeMode.FIT)

    val resizeMode = _resizeMode.asStateFlow()


    fun setResizeMode(mode: VideoResizeMode) {
        _resizeMode.value = mode
    }

    fun cycleResizeMode() {
        _resizeMode.value = when (_resizeMode.value) {
            VideoResizeMode.FIT -> VideoResizeMode.FILL
            VideoResizeMode.FILL -> VideoResizeMode.ZOOM
            VideoResizeMode.ZOOM -> VideoResizeMode.STRETCH
            VideoResizeMode.STRETCH -> VideoResizeMode.FIT
        }
    }

    fun loadMedia(media: PlayerMedia) {

        progressJob?.cancel()

        player.setMediaItem(
            MediaItem.fromUri(media.videoUri)
        )



        player.prepare()

        if(media.resume > 0){

            player.seekTo(media.resume)
        }

        _playerState.update {

            it.copy(

                media = media,

                duration = media.duration,

                currentPosition = 0,

                bufferedPosition = 0,

                error = null
            )
        }
    }

    fun loadPlaylist(

        playlist: List<PlayerMedia>,

        startIndex: Int

    ){
        _playerState.value =

            _playerState.value.copy(

                playlist = playlist,

                currentIndex = startIndex,

                media = playlist[startIndex]
            )

        loadMedia(playlist[startIndex])
    }



    private fun updateTracks() {

        val audioTracks = mutableListOf<AudioTrack>()

        val subtitleTracks = mutableListOf<SubtitleTrack>()

        player.currentTracks.groups.forEachIndexed { groupIndex, group ->

            when (group.type) {

                C.TRACK_TYPE_AUDIO -> {

                    repeat(group.length) { trackIndex ->

                        val format = group.getTrackFormat(trackIndex)

                        audioTracks += AudioTrack(

                            groupIndex = groupIndex,

                            trackIndex = trackIndex,

                            language = format.language,

                            label =
                                format.label
                                    ?: format.language
                                    ?: "Audio ${trackIndex + 1}",

                            selected =
                                group.isTrackSelected(trackIndex)
                        )
                    }
                }

                C.TRACK_TYPE_TEXT -> {

                    repeat(group.length) { trackIndex ->

                        val format = group.getTrackFormat(trackIndex)

                        subtitleTracks += SubtitleTrack(

                            groupIndex = groupIndex,

                            trackIndex = trackIndex,

                            language = format.language,

                            label =
                                format.label
                                    ?: format.language
                                    ?: "Subtitle ${trackIndex + 1}",

                            selected =
                                group.isTrackSelected(trackIndex)
                        )
                    }
                }
            }
        }

        subtitleTracks.add(
            0,
            SubtitleTrack(
                groupIndex = -1,
                trackIndex = -1,
                language = null,
                label = "Off",
                selected = subtitleTracks.none { it.selected },
                isOff = true
            )
        )

        Log.d("Anidex_PLAYER",
            "Tracks : ${audioTracks.size} | SubTitle Tracks ${subtitleTracks.size}")


        _audioTracks.value = audioTracks
        _subtitleTracks.value = subtitleTracks

        _playerState.update {

            it.copy(

                audioTracks = audioTracks,

                subtitleTracks = subtitleTracks
            )
        }
    }


    private val _playbackSpeed =
        MutableStateFlow(1.0f)

    val playbackSpeed = _playbackSpeed.asStateFlow()

    fun setPlaybackSpeed(speed: Float) {

        player.setPlaybackSpeed(speed)

        _playbackSpeed.value = speed
    }


    fun play() {

        player.play()
    }

    fun pause() {

        player.pause()
        onProgress?.invoke(playerState.value)
    }

    fun togglePlayPause() {

        if (player.isPlaying)
            pause()
        else
            play()
    }

    fun stop() {

        player.stop()

        progressJob?.cancel()

        onProgress?.invoke(playerState.value)

        _playerState.update {

            it.copy(

                isPlaying = false,

                currentPosition = 0,

                bufferedPosition = 0

            )
        }
    }

    fun replay() {
        seekTo(0)
        play()
    }

    fun seekTo(position: Long) {

        val newPosition = position.coerceIn(
            0L,
            player.duration.coerceAtLeast(0L)
        )

        player.seekTo(newPosition)

        _playerState.update {
            it.copy(
                currentPosition = newPosition
            )
        }
    }
    fun seekForward() {

        player.seekTo(

            player.currentPosition + SEEK_AMOUNT

        )
    }

    fun seekBackward() {

        player.seekTo(

            (player.currentPosition - SEEK_AMOUNT)
                .coerceAtLeast(0)

        )
    }


    fun selectAudioTrack(track: AudioTrack) {

        val group =
            player.currentTracks.groups[track.groupIndex]

        val override = TrackSelectionOverride(

            group.mediaTrackGroup,

            listOf(track.trackIndex)
        )

        player.trackSelectionParameters =

            player.trackSelectionParameters
                .buildUpon()
                .clearOverridesOfType(C.TRACK_TYPE_AUDIO)
                .addOverride(override)
                .build()

        updateTracks()
    }

    fun selectSubtitleTrack(track: SubtitleTrack) {

        val builder =
            player.trackSelectionParameters
                .buildUpon()

        if (track.isOff) {

            builder
                .setTrackTypeDisabled(
                    C.TRACK_TYPE_TEXT,
                    true
                )

        } else {

            val group =
                player.currentTracks.groups[track.groupIndex]

            val override =
                TrackSelectionOverride(

                    group.mediaTrackGroup,

                    listOf(track.trackIndex)
                )

            builder
                .setTrackTypeDisabled(
                    C.TRACK_TYPE_TEXT,
                    false
                )
                .clearOverridesOfType(C.TRACK_TYPE_TEXT)
                .addOverride(override)
        }

        player.trackSelectionParameters =
            builder.build()

        updateTracks()
    }


    fun disableSubtitles() {

        val parameters =
            player.trackSelectionParameters
                .buildUpon()
                .setTrackTypeDisabled(
                    C.TRACK_TYPE_TEXT,
                    true
                )
                .build()

        player.trackSelectionParameters = parameters
    }

    private val _audioTracks =
        MutableStateFlow<List<AudioTrack>>(emptyList())

    val audioTracks: StateFlow<List<AudioTrack>> =
        _audioTracks.asStateFlow()

    private val _subtitleTracks =
        MutableStateFlow<List<SubtitleTrack>>(emptyList())

    val subtitleTracks: StateFlow<List<SubtitleTrack>> =
        _subtitleTracks.asStateFlow()


    private fun startProgressUpdates() {

        progressJob?.cancel()

        progressJob =

            scope.launch {
                var lastSave = 0L


                while (isActive) {

                    _playerState.update {

                        it.copy(
                            currentPosition = player.currentPosition,
                            duration = player.duration.coerceAtLeast(0L),
                            bufferedPosition = player.bufferedPosition
                        )
                    }

                    if(player.currentPosition - lastSave >= 5000){

                        lastSave = player.currentPosition

                        onProgress?.invoke(_playerState.value)
                    }

                    delay(UPDATE_INTERVAL)
                }
            }
    }


    fun release() {

        progressJob?.cancel()

        scope.cancel()
        onProgress?.invoke(playerState.value)
        player.release()
    }
}