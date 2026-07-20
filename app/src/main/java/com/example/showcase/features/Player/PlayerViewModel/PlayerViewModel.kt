package com.example.showcase.features.Player.PlayerViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.InfoPage.uistate.MovieInfoUiModel
import com.example.showcase.features.InfoPage.uistate.SeriesInfoUiModel

import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.model.AudioTrack
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.model.PlayerInfoState
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.SubtitleTrack
import com.example.showcase.features.Player.model.TrackSheetType
import com.example.showcase.features.Player.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.get

class PlayerViewModel(

    private val manager: PlayerManager,

    private val repository: PlayerRepository

) : ViewModel() {

    init{

        manager.onProgress = {

            viewModelScope.launch {

                repository.savePlayback(it)
            }
        }

        manager.onPlaybackEnded = {

            viewModelScope.launch {

                repository.savePlayback(state.value)

                playNextEpisode()
            }
        }


    }

    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen = _isFullscreen.asStateFlow()

    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }

    fun setFullscreen(value: Boolean) {
        _isFullscreen.value = value
    }

    val state = manager.playerState

    private val _infoState =
        MutableStateFlow(PlayerInfoState())

    val infoState =
        _infoState.asStateFlow()


    fun startSeries(seriesId: Long) {

        viewModelScope.launch {

            repository
                .getSeriesPlayback(seriesId)
                ?.let { playback ->

                    manager.loadPlaylist(

                        playback.playlist,

                        playback.startIndex
                    )

                    // Load series metadata
                    repository.loadSeriesInfo(seriesId).let { info ->
                        _infoState.value =
                            PlayerInfoState(series = info)
                    }

                    Log.d(
                        "Anidex_PlayerVM",
                        "infoState title = ${_infoState.value.series?.metadata?.title}"
                    )

                    manager.play()
                }
        }
    }

    private fun loadMetadata(media: PlayerMedia) {

        viewModelScope.launch {

            _infoState.value =
                PlayerInfoState(
                    loading = true
                )

            runCatching {

                when(media.type){

                    MediaType.MOVIE ->
                        repository.loadMovieInfo(media.id)

                    MediaType.EPISODE ->
                        repository.loadSeriesInfo(media.id)
                }

            }.onSuccess {

                when(it){

                    is MovieInfoUiModel ->

                        _infoState.value =
                            PlayerInfoState(
                                movie = it
                            )

                    is SeriesInfoUiModel ->

                        _infoState.value =
                            PlayerInfoState(
                                series = it
                            )
                }

            }.onFailure {

                _infoState.value =
                    PlayerInfoState(
                        error = it.message
                    )
            }

        }

    }


    fun playEpisode(index: Int) {

        val playlist = state.value.playlist

        if(index !in playlist.indices)
            return

        viewModelScope.launch{

            repository.savePlayback(state.value)

            manager.loadPlaylist(playlist,index)

            manager.play()
        }

    }
    fun startMovie(movieId: Long) {

        viewModelScope.launch {

            repository
                .getMovie(movieId)
                ?.let {

                    manager.loadMedia(it)

                    loadMetadata(it)

                    manager.play()
                }
        }
    }
    val audioTracks = manager.audioTracks
    val subtitleTracks = manager.subtitleTracks


    fun disableSubtitles() {
        manager.disableSubtitles()
    }


    fun playNextEpisode() {

        val nextIndex = state.value.currentIndex + 1

        if (nextIndex >= state.value.playlist.size)
            return

        playEpisode(nextIndex)
    }



    fun playPreviousEpisode() {

        val previousIndex = state.value.currentIndex - 1

        if (previousIndex < 0)
            return

        playEpisode(previousIndex)
    }

    fun play() = manager.play()

    fun pause() = manager.pause()

    fun seek(position:Long)=
        manager.seekTo(position)

    fun forward10()=
        manager.seekForward()

    fun rewind10()=
        manager.seekBackward()

    fun selectAudio(track: AudioTrack) {

        manager.selectAudioTrack(track)
    }

    fun selectSubtitle(track: SubtitleTrack) {

        manager.selectSubtitleTrack(track)
    }

    private val _trackSheet =
        MutableStateFlow(TrackSheetType.NONE)

    val trackSheet = _trackSheet.asStateFlow()

    fun showSubtitleSheet() {
        _trackSheet.value = TrackSheetType.SUBTITLE
    }

    fun showAudioSheet() {
        _trackSheet.value = TrackSheetType.AUDIO
    }

    fun hideSheet() {
        _trackSheet.value = TrackSheetType.NONE
    }

}