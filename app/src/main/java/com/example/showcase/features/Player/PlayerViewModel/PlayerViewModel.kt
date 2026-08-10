package com.example.showcase.features.Player.PlayerViewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.core.SettingesManger.SettingsManager
import com.example.showcase.core.ProgressManager.ProgressManager
import com.example.showcase.features.InfoPage.model.MovieInfoUiModel
import com.example.showcase.features.InfoPage.model.SeriesInfoUiModel
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerCore.VlcPlayerManager
import com.example.showcase.features.Player.model.AudioTrack
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.features.Player.model.SubtitleTrack
import com.example.showcase.features.Player.model.TrackSheetType
import com.example.showcase.features.Player.PlayerCore.repository.PlayerRepository
import com.example.showcase.features.Player.ui.formatTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


enum class PlayerEngine {
    EXO,
    VLC
}


class PlayerViewModel(

    private val manager: PlayerManager,

    private val vlcManager: VlcPlayerManager,

    private val repository: PlayerRepository

) : ViewModel() {

    private val _playerEngine =
        MutableStateFlow(SettingsManager.playerEngine)

    val playerEngine =
        _playerEngine.asStateFlow()

    private fun fallbackToVlc() {
        if(_playerEngine.value == PlayerEngine.EXO){
            switchToVlc()
        }
    }

    fun setPlayerEngine(engine: PlayerEngine) {

        _playerEngine.value = engine

        Log.d(
            "Anidex_PlayerVM",
            "Player engine changed to $engine"
        )
    }

    fun togglePlayerEngine(){

        when(_playerEngine.value){
            PlayerEngine.EXO -> switchToVlc()
            PlayerEngine.VLC -> switchToExo()
        }
    }

    private fun switchToVlc() {

        val state = manager.playerState.value
        val media = state.media ?: return

        if (_playerEngine.value == PlayerEngine.VLC) {
            Log.d(
                "Anidex_vm",
                "Player engine is already VLC"
            )
            return
        }
        val resumePosition =
            if (state.currentPosition > 0L) {
                state.currentPosition
            } else {
                media.resume
            }

        Log.d(
            "Anidex_vm",
            "Switching EXO -> VLC"
        )

        Log.d(
            "Anidex_vm",
            "EXO live position = ${
                formatTime(state.currentPosition)
            }"
        )

        Log.d(
            "Anidex_vm",
            "Saved media position = ${
                formatTime(media.resume)
            }"
        )

        Log.d(
            "Anidex_vm",
            "VLC resume position = ${
                formatTime(resumePosition)
            }"
        )

        manager.stop()

        val updated = media.copy(
            resume = resumePosition
        )

        _playerEngine.value = PlayerEngine.VLC

        if (state.playlist.isEmpty()) {

            vlcManager.loadMedia(
                updated
            )

        } else {

            val playlist =
                state.playlist.toMutableList()

            playlist[state.currentIndex] =
                updated

            vlcManager.loadPlaylist(
                playlist,
                state.currentIndex
            )
        }

        vlcManager.play()

        Log.d(
            "Anidex_vm",
            "VLC started at ${
                formatTime(resumePosition)
            }"
        )
    }


    private fun switchToExo(){

        val state = vlcManager.playerState.value

        val media = state.media ?: return

        if (_playerEngine.value == PlayerEngine.EXO)
        {
            Log.d("Anidex_vm","player engine is Already set to vlc")
            return
        }
        vlcManager.stop()

        val updated = media.copy(
            resume = state.currentPosition
        )

        _playerEngine.value = PlayerEngine.EXO

        if(state.playlist.isEmpty()){

            manager.loadMedia(updated)

        }else{

            val playlist = state.playlist.toMutableList()

            playlist[state.currentIndex] = updated

            manager.loadPlaylist(
                playlist,
                state.currentIndex
            )
        }

        manager.play()
        Log.d(
            "Anidex_PlayerVM",
            "Player engine changed to EXO"
        )
    }

    private fun activeState() =
        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.playerState

            PlayerEngine.VLC ->
                vlcManager.playerState
        }


    val state: StateFlow<PlayerState> =
        combine(
            manager.playerState,
            vlcManager.playerState,
            _playerEngine
        ) { exoState, vlcState, engine ->

            when (engine) {
                PlayerEngine.EXO -> exoState
                PlayerEngine.VLC -> vlcState
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PlayerState()
        )


    init {
        manager.onProgress = { playerState ->
            ProgressManager.updatePlayer(
                playerState
            )
            viewModelScope.launch {
                repository.savePlayback(
                    playerState
                )
                Log.d("Anidex_VM","Saved PlayBack Va EXO " + formatTime( playerState.currentPosition))
            }
        }


        manager.onPlaybackEnded = {
            ProgressManager.hide()
            viewModelScope.launch {

                repository.savePlayback(
                    manager.playerState.value
                )
                if (SettingsManager.autoPlayNextEpisode) playNextEpisode()
            }
        }


        vlcManager.onProgress = { playerState ->
            ProgressManager.updatePlayer(
                playerState
            )
            viewModelScope.launch {

                repository.savePlayback(
                    playerState
                )
                Log.d("Anidex_VM","Saved PlayBack Va Vlc " + formatTime( playerState.currentPosition))
            }
        }

        vlcManager.onPlaybackEnded = {
            ProgressManager.hide()
            viewModelScope.launch {

                repository.savePlayback(
                    vlcManager.playerState.value
                )

                playNextEpisode()
            }
        }


        manager.onError = { playerState ->

            val media = playerState.media

            if (media != null) {
                Log.d("Anidex_VM","Switching TO VLC")
                fallbackToVlc()
            }
        }
    }



    private val _isFullscreen =
        MutableStateFlow(false)

    val isFullscreen =
        _isFullscreen.asStateFlow()


    fun toggleFullscreen() {

        _isFullscreen.value =
            !_isFullscreen.value
    }


    fun setFullscreen(
        value: Boolean
    ) {

        _isFullscreen.value =
            value
    }




    private val _infoState =
        MutableStateFlow(
            PlayerInfoState()
        )

    val infoState =
        _infoState.asStateFlow()



    fun startSeries(
        seriesId: Long
    ) {

        viewModelScope.launch {

            repository
                .getSeriesPlayback(seriesId)
                ?.let { playback ->

                    when (_playerEngine.value) {

                        PlayerEngine.EXO -> {

                            manager.loadPlaylist(
                                playback.playlist,
                                playback.startIndex
                            )

                            manager.play()
                        }


                        PlayerEngine.VLC -> {

                            vlcManager.loadPlaylist(
                                playback.playlist,
                                playback.startIndex
                            )

                            vlcManager.play()
                        }
                    }


                    repository
                        .loadSeriesInfo(seriesId)
                        .let { info ->

                            _infoState.value =
                                PlayerInfoState(
                                    series = info
                                )
                        }


                    Log.d(
                        "Anidex_PlayerVM",
                        "infoState title = " +
                                _infoState.value
                                    .series
                                    ?.metadata
                                    ?.title
                    )
                }
        }
    }


    private fun loadMetadata(
        media: PlayerMedia
    ) {

        viewModelScope.launch {

            _infoState.value =
                PlayerInfoState(
                    loading = true
                )


            runCatching {

                when (media.type) {

                    MediaType.MOVIE ->
                        repository
                            .loadMovieInfo(
                                media.id
                            )

                    MediaType.EPISODE ->
                        repository
                            .loadSeriesInfo(
                                media.id
                            )
                }

            }.onSuccess {

                when (it) {

                    is MovieInfoUiModel -> {

                        _infoState.value =
                            PlayerInfoState(
                                movie = it
                            )
                    }


                    is SeriesInfoUiModel -> {

                        _infoState.value =
                            PlayerInfoState(
                                series = it
                            )
                    }
                }

            }.onFailure {

                _infoState.value =
                    PlayerInfoState(
                        error = it.message
                    )
            }
        }
    }



    fun playEpisode(
        index: Int
    ) {

        val playlist =
            state.value.playlist


        if (
            index !in playlist.indices
        ) {
            return
        }


        viewModelScope.launch {

            repository.savePlayback(
                state.value
            )


            when (_playerEngine.value) {

                PlayerEngine.EXO -> {

                    manager.loadPlaylist(
                        playlist,
                        index
                    )

                    manager.play()
                }


                PlayerEngine.VLC -> {

                    vlcManager.loadPlaylist(
                        playlist,
                        index
                    )

                    vlcManager.play()
                }
            }
        }
    }




    fun startMovie(
        movieId: Long
    ) {

        viewModelScope.launch {

            repository
                .getMovie(movieId)
                ?.let { media ->


                    when (_playerEngine.value) {

                        PlayerEngine.EXO -> {

                            manager.loadMedia(
                                media
                            )

                            manager.play()
                        }


                        PlayerEngine.VLC -> {

                            vlcManager.loadMedia(
                                media
                            )

                            vlcManager.play()
                        }
                    }


                    loadMetadata(
                        media
                    )
                }
        }
    }



    val audioTracks: StateFlow<List<AudioTrack>> =
        combine(
            manager.audioTracks,
            vlcManager.audioTracks,
            _playerEngine
        ) { exoTracks, vlcTracks, engine ->

            when (engine) {
                PlayerEngine.EXO -> exoTracks
                PlayerEngine.VLC -> vlcTracks
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    val subtitleTracks: StateFlow<List<SubtitleTrack>> =
        combine(
            manager.subtitleTracks,
            vlcManager.subtitleTracks,
            _playerEngine
        ) { exoTracks, vlcTracks, engine ->

            when (engine) {
                PlayerEngine.EXO -> exoTracks
                PlayerEngine.VLC -> vlcTracks
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    fun disableSubtitles() {

        when (_playerEngine.value) {

            PlayerEngine.EXO -> {
                manager.disableSubtitles()
            }

            PlayerEngine.VLC -> {
                vlcManager.disableSubtitles()
            }
        }
    }


    fun playNextEpisode() {

        val nextIndex =
            state.value.currentIndex + 1


        if (
            nextIndex >=
            state.value.playlist.size
        ) {
            return
        }


        playEpisode(
            nextIndex
        )
    }


    fun playPreviousEpisode() {

        val previousIndex =
            state.value.currentIndex - 1


        if (
            previousIndex < 0
        ) {
            return
        }


        playEpisode(
            previousIndex
        )
    }



    fun play() {

        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.play()

            PlayerEngine.VLC ->
                vlcManager.play()
        }
    }


    fun pause() {

        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.pause()

            PlayerEngine.VLC ->
                vlcManager.pause()
        }
    }


    fun togglePlayPause() {

        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.togglePlayPause()

            PlayerEngine.VLC ->
                vlcManager.togglePlayPause()
        }
    }


    fun seek(
        position: Long
    ) {

        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.seekTo(
                    position
                )

            PlayerEngine.VLC ->
                vlcManager.seekTo(
                    position
                )
        }
    }


    fun seekToProgress(
        progress: Float
    ) {

        val duration =
            state.value.duration


        if (
            duration <= 0L
        ) {
            return
        }


        val position =
            (
                    duration *
                            progress
                    )
                .toLong()
                .coerceIn(
                    0L,
                    duration
                )


        seek(
            position
        )
    }


    fun forward10() {

        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.seekForward()

            PlayerEngine.VLC ->
                vlcManager.seekForward()
        }
    }


    fun rewind10() {

        when (_playerEngine.value) {

            PlayerEngine.EXO ->
                manager.seekBackward()

            PlayerEngine.VLC ->
                vlcManager.seekBackward()
        }
    }




    fun selectAudio(
        track: AudioTrack
    ) {

        when (_playerEngine.value) {

            PlayerEngine.EXO -> {

                manager.selectAudioTrack(
                    track
                )
            }

            PlayerEngine.VLC -> {

                vlcManager.selectAudioTrack(
                    track
                )
            }
        }
    }



    fun selectSubtitle(
        track: SubtitleTrack
    ) {

        when (_playerEngine.value) {

            PlayerEngine.EXO -> {

                manager.selectSubtitleTrack(
                    track
                )
            }

            PlayerEngine.VLC -> {

                vlcManager.selectSubtitleTrack(
                    track
                )
            }
        }
    }



    private val _trackSheet =
        MutableStateFlow(
            TrackSheetType.NONE
        )

    val trackSheet =
        _trackSheet.asStateFlow()


    fun showSubtitleSheet() {

        _trackSheet.value =
            TrackSheetType.SUBTITLE
    }


    fun showAudioSheet() {

        _trackSheet.value =
            TrackSheetType.AUDIO
    }


    fun hideSheet() {

        _trackSheet.value =
            TrackSheetType.NONE
    }
}