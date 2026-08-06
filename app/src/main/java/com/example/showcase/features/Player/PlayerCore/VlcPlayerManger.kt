package com.example.showcase.features.Player.PlayerCore

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.util.Log
import android.view.SurfaceView

import com.example.showcase.features.Player.model.AudioTrack
import com.example.showcase.features.Player.model.PlayerMedia
import com.example.showcase.features.Player.model.PlayerState
import com.example.showcase.features.Player.model.SubtitleTrack

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import android.os.ParcelFileDescriptor
import android.view.Surface
import android.view.TextureView
import com.example.showcase.core.SettingesManger.SettingsManager
import java.io.File


class VlcPlayerManager(
	context: Context
) {
	companion object {
		private const val TAG = "Anidex_VLC"
		private const val UPDATE_INTERVAL = 500L
		 val SEEK_AMOUNT = SettingsManager.seekAmount
	}


	private var currentPfd: ParcelFileDescriptor? = null
	private val appContext = context.applicationContext
	val libVLC = LibVLC(
		appContext, arrayListOf(
			"--audio-time-stretch", "--network-caching=150"
		)
	)

	val player = MediaPlayer(
		libVLC
	)

	private val scope = CoroutineScope(
		Dispatchers.Main + SupervisorJob()
	)
	private var progressJob: Job? = null
	private val _playerState = MutableStateFlow(
		PlayerState()
	)


	val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

	var onPlaybackEnded: (() -> Unit)? = null
	var onProgress: ((PlayerState) -> Unit)? = null
	private var currentMedia: Media? = null
	private var surfaceView: SurfaceView? = null


	init {

		player.setEventListener { event ->

			handleEvent(
				event
			)
		}
	}

	private var textureView: TextureView? = null
	private var textureSurface: Surface? = null

	fun attachTextureView(textureView: TextureView) {
		textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
			override fun onSurfaceTextureAvailable(
				surface: SurfaceTexture, width: Int, height: Int
			) {
				val vlcVout = player.vlcVout
				if (!vlcVout.areViewsAttached()) {
					vlcVout.setVideoView(textureView)
					vlcVout.setWindowSize(width, height)
					vlcVout.attachViews()
				}
			}

			override fun onSurfaceTextureSizeChanged(
				surface: SurfaceTexture, width: Int, height: Int
			) {
				player.vlcVout.setWindowSize(width, height)
			}

			override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
				val vlcVout = player.vlcVout
				if (vlcVout.areViewsAttached()) {
					vlcVout.detachViews()
				}
				return true
			}

			override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
		}

		// If the surface texture is already available when method is called:
		if (textureView.isAvailable) {
			val vlcVout = player.vlcVout
			if (!vlcVout.areViewsAttached()) {
				vlcVout.setVideoView(textureView)
				vlcVout.setWindowSize(textureView.width, textureView.height)
				vlcVout.attachViews()
			}
		}
	}


	private val _resizeMode = MutableStateFlow(PlayerManager.VideoResizeMode.FIT)
	val resizeMode: StateFlow<PlayerManager.VideoResizeMode> = _resizeMode.asStateFlow()

	fun setResizeMode(mode: PlayerManager.VideoResizeMode) {
		_resizeMode.value = mode
	}

	fun cycleResizeMode() {
		_resizeMode.value = when (_resizeMode.value) {
			PlayerManager.VideoResizeMode.FIT -> PlayerManager.VideoResizeMode.FILL
			PlayerManager.VideoResizeMode.FILL -> PlayerManager.VideoResizeMode.ZOOM
			PlayerManager.VideoResizeMode.ZOOM -> PlayerManager.VideoResizeMode.STRETCH
			PlayerManager.VideoResizeMode.STRETCH -> PlayerManager.VideoResizeMode.FIT
		}
	}

	fun applyResizeMode(
		mode: PlayerManager.VideoResizeMode, containerWidth: Int, containerHeight: Int
	) {
		when (mode) {
			PlayerManager.VideoResizeMode.FIT -> {
				// Native video aspect ratio with letterboxing
				player.aspectRatio = null
				player.scale = 0f
			}

			PlayerManager.VideoResizeMode.STRETCH -> {
				// Force the video to match the display container's aspect ratio
				if (containerWidth > 0 && containerHeight > 0) {
					player.aspectRatio = "$containerWidth:$containerHeight"
				}
				player.scale = 0f
			}

			PlayerManager.VideoResizeMode.FILL -> {
				// Stretches/fills parent frame
				player.aspectRatio = null
				player.scale = 1.0f
			}

			PlayerManager.VideoResizeMode.ZOOM -> {
				// Zooms past bounds to crop black bars
				player.aspectRatio = null
				player.scale = 1.35f
			}
		}
	}

	fun setVideoScale(resizeMode: PlayerManager.VideoResizeMode) {
		when (resizeMode) {
			PlayerManager.VideoResizeMode.FIT -> {
				player.aspectRatio = null // Restores default native aspect ratio
				player.scale = 0f
			}

			PlayerManager.VideoResizeMode.FILL, PlayerManager.VideoResizeMode.STRETCH -> {
				// Adjusts to match container boundaries
				player.aspectRatio =
					"16:9" // Or dynamically calculate screen ratio (e.g. "16:9", "4:3")
				player.scale = 0f
			}

			PlayerManager.VideoResizeMode.ZOOM -> {
				player.scale = 1.25f
			}
		}
	}


	fun detachTextureView() {
		// In VlcPlayerManager.kt

		val vlcVout = player.vlcVout
		if (vlcVout.areViewsAttached()) {
			vlcVout.detachViews()
		}
	}


// ---------------------------------------------------------
// VLC SURFACE ATTACH / DETACH
// ---------------------------------------------------------

	private fun attachVlcSurface(
		surface: Surface
	) {

		try {

			player.vlcVout.apply {

				setVideoSurface(
					surface, null
				)

				attachViews()
			}

			Log.d(
				TAG, "VLC TextureView surface attached"
			)

		} catch (e: Exception) {

			Log.e(
				TAG, "Failed to attach VLC TextureView surface", e
			)
		}
	}


	private fun detachVlcSurface() {

		try {

			player.vlcVout.detachViews()

			Log.d(
				TAG, "VLC TextureView surface detached"
			)

		} catch (e: Exception) {

			Log.e(
				TAG, "Failed to detach VLC TextureView surface", e
			)
		}
	}

	fun attachSurface(
		surface: SurfaceView
	) {

		surfaceView = surface


		player.vlcVout.apply {

			setVideoView(
				surface
			)

			attachViews()
		}


		Log.d(
			TAG, "Surface attached"
		)
	}


	fun detachSurface() {

		try {

			player.vlcVout.detachViews()

			surfaceView = null

		} catch (
			e: Exception
		) {

			Log.e(
				TAG, "Failed to detach surface", e
			)
		}
	}


	// ---------------------------------------------------------
	// LOAD MEDIA
	// ---------------------------------------------------------
	fun loadMedia(media: PlayerMedia) {

		progressJob?.cancel()

		scope.launch {

			val resolvedUri =
				resolveMediaUri(
					media.videoUri
				)

			if (resolvedUri == null) {

				Log.e(
					TAG,
					"Unable to resolve media URI: ${media.videoUri}"
				)

				_playerState.update {

					it.copy(
						isPlaying = false,
						isBuffering = false,
						error = "Unable to access media file"
					)
				}

				return@launch
			}

			Log.d(
				TAG,
				"VLC resolved URI: $resolvedUri"
			)

			val vlcMedia =
				Media(
					libVLC,
					resolvedUri
				)

			player.stop()

			player.media =
				vlcMedia

			vlcMedia.release()

			_playerState.update {

				it.copy(
					media = media,
					duration = media.duration,
					currentPosition = 0L,
					bufferedPosition = 0L,
					isPlaying = false,
					isEnded = false,
					error = null
				)
			}

			player.play()

			if (media.resume > 0L) {

				delay(500)

				val duration =
					player.length

				val resumePosition =
					if (duration > 0L) {

						media.resume.coerceIn(
							0L,
							duration
						)

					} else {

						media.resume
					}

				player.time =
					resumePosition

				_playerState.update {

					it.copy(
						currentPosition =
							resumePosition
					)
				}

				Log.d(
					TAG,
					"Resumed playback at $resumePosition ms"
				)
			}
		}
	}
	// ---------------------------------------------------------
	// PLAYLIST
	// Same behavior as ExoPlayer
	// ---------------------------------------------------------

	fun loadPlaylist(

		playlist: List<PlayerMedia>,

		startIndex: Int

	) {

		if (startIndex !in playlist.indices) {

			return
		}


		_playerState.value = _playerState.value.copy(

			playlist = playlist,

			currentIndex = startIndex,

			media = playlist[startIndex]
		)


		loadMedia(
			playlist[startIndex]
		)
	}

	private fun updatePlayerPosition() {

		val position = player.time.coerceAtLeast(0L)

		val duration = player.length.coerceAtLeast(0L)

		_playerState.update {

			it.copy(

				currentPosition = position,

				duration = if (duration > 0L) duration
				else it.duration,

				bufferedPosition = position
			)
		}
	}


	private fun startProgressUpdates() {

		progressJob?.cancel()

		progressJob = scope.launch {

			var lastSave = 0L

			while (isActive) {

				updatePlayerPosition()

				val currentPosition = _playerState.value.currentPosition

				// Save every 5 seconds
				if (currentPosition - lastSave >= 5000L) {

					lastSave = currentPosition

					onProgress?.invoke(
						_playerState.value
					)
				}

				delay(500L)
			}
		}
	}

	// ---------------------------------------------------------
	// PLAYBACK
	// ---------------------------------------------------------

	fun play() {

		player.play()
	}


	fun pause() {

		player.pause()

		onProgress?.invoke(
			playerState.value
		)
	}


	fun togglePlayPause() {

		if (player.isPlaying) {

			pause()

		} else {

			play()
		}
	}

	fun stop() {

		updatePlayerPosition()

		player.stop()

		progressJob?.cancel()

		// Save current position to Room
		onProgress?.invoke(
			_playerState.value
		)

		_playerState.update {
			it.copy(
				isPlaying = false, currentPosition = 0L, bufferedPosition = 0L
			)
		}
	}


	fun replay() {

		seekTo(
			0L
		)

		play()
	}


	// ---------------------------------------------------------
	// SEEK
	// ---------------------------------------------------------

	fun seekTo(
		position: Long
	) {

		val duration = player.length.coerceAtLeast(
				0L
			)


		val newPosition =

			position.coerceIn(

				0L,

				duration
			)


		player.setTime(
			newPosition
		)


		_playerState.update {

			it.copy(

				currentPosition = newPosition
			)
		}
	}


	fun seekForward() {

		seekTo(

			currentPosition + SEEK_AMOUNT
		)
	}


	fun seekBackward() {

		seekTo(

			(currentPosition - SEEK_AMOUNT).coerceAtLeast(
					0L
				)
		)
	}


	// ---------------------------------------------------------
	// CURRENT POSITION
	// ---------------------------------------------------------

	private val currentPosition: Long
		get() = player.time.coerceAtLeast(
				0L
			)


	// ---------------------------------------------------------
	// PLAYBACK SPEED
	// ---------------------------------------------------------

	private val _playbackSpeed = MutableStateFlow(
		1.0f
	)


	val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()


	fun setPlaybackSpeed(
		speed: Float
	) {

		if (speed <= 0f) {

			return
		}


		player.setRate(
			speed
		)


		_playbackSpeed.value = speed
	}


	private val _audioTracks =
		MutableStateFlow<List<AudioTrack>>(emptyList())

	val audioTracks: StateFlow<List<AudioTrack>> =
		_audioTracks.asStateFlow()


	private val _subtitleTracks =
		MutableStateFlow<List<SubtitleTrack>>(emptyList())

	val subtitleTracks: StateFlow<List<SubtitleTrack>> =
		_subtitleTracks.asStateFlow()
// =========================================================
// VLC TRACKS
// =========================================================

	private fun updateTracks() {

		val audio = mutableListOf<AudioTrack>()
		val subtitles = mutableListOf<SubtitleTrack>()

		// -----------------------------
		// AUDIO TRACKS
		// -----------------------------

		player.audioTracks?.forEach { track ->

			if (track.id >= 0) {

				audio += AudioTrack(
					groupIndex = -1,
					trackIndex = track.id,
					language = track.name,
					label = track.name ?: "Audio ${audio.size + 1}",
					selected = track.id == player.audioTrack
				)
			}
		}


		// -----------------------------
		// SUBTITLE TRACKS
		// -----------------------------

		subtitles += SubtitleTrack(
			groupIndex = -1,
			trackIndex = -1,
			language = null,
			label = "Off",
			selected = player.spuTrack == -1,
			isOff = true
		)


		player.spuTracks?.forEach { track ->

			if (track.id >= 0) {

				subtitles += SubtitleTrack(
					groupIndex = -1,
					trackIndex = track.id,
					language = track.name,
					label = track.name ?: "Subtitle ${subtitles.size}",
					selected = track.id == player.spuTrack,
					isOff = false
				)
			}
		}


		_audioTracks.value = audio
		_subtitleTracks.value = subtitles


		_playerState.update {

			it.copy(
				audioTracks = audio,
				subtitleTracks = subtitles
			)
		}


		Log.d(
			TAG,
			"VLC tracks -> audio=${audio.size}, subtitles=${subtitles.size}"
		)
	}

	fun selectAudioTrack(
		track: AudioTrack
	) {

		if (track.trackIndex < 0) {
			return
		}

		try {

			player.audioTrack =
				track.trackIndex

			updateTracks()

			Log.d(
				TAG,
				"Selected VLC audio track: ${track.trackIndex}"
			)

		} catch (e: Exception) {

			Log.e(
				TAG,
				"Failed to select VLC audio track",
				e
			)
		}
	}

	fun selectSubtitleTrack(
		track: SubtitleTrack
	) {

		try {

			if (track.isOff) {

				player.spuTrack = -1

				Log.d(
					TAG,
					"VLC subtitles disabled"
				)

			} else {

				if (track.trackIndex < 0) {
					return
				}

				player.spuTrack =
					track.trackIndex

				Log.d(
					TAG,
					"Selected VLC subtitle track: ${track.trackIndex}"
				)
			}

			updateTracks()

		} catch (e: Exception) {

			Log.e(
				TAG,
				"Failed to select VLC subtitle track",
				e
			)
		}
	}

	fun disableSubtitles() {

		try {

			player.spuTrack = -1

			updateTracks()

			Log.d(
				TAG,
				"VLC subtitles disabled"
			)

		} catch (e: Exception) {

			Log.e(
				TAG,
				"Failed to disable VLC subtitles",
				e
			)
		}
	}

	// ---------------------------------------------------------
	// EVENT HANDLER
	// ---------------------------------------------------------

	private fun handleEvent(

		event: MediaPlayer.Event

	) {

		when (event.type) {

			MediaPlayer.Event.Opening -> {

				Log.d(
					TAG, "Opening"
				)


				_playerState.update {

					it.copy(

						isBuffering = true,

						isEnded = false,

						error = null,

						audioTracks = emptyList(),

						subtitleTracks = emptyList()
					)
				}

			}


			MediaPlayer.Event.Buffering -> {

				_playerState.update {

					it.copy(

						isBuffering = event.buffering < 100f
					)
				}
			}


			MediaPlayer.Event.Playing -> {

				Log.d(
					TAG, "Playing"
				)


				_playerState.update {

					it.copy(

						isPlaying = true,

						isBuffering = false,

						isEnded = false,

						error = null
					)
				}
				updateTracks()

				startProgressUpdates()
			}


			MediaPlayer.Event.Paused -> {

				Log.d(
					TAG, "Paused"
				)


				_playerState.update {

					it.copy(

						isPlaying = false,

						isBuffering = false,

						currentPosition = currentPosition
					)
				}
			}


			MediaPlayer.Event.Stopped -> {

				Log.d(
					TAG, "Stopped"
				)


				_playerState.update {

					it.copy(

						isPlaying = false,

						isBuffering = false
					)
				}
			}


			MediaPlayer.Event.TimeChanged -> {
			}


			MediaPlayer.Event.EndReached -> {

				val duration = player.length.coerceAtLeast(0L)

				_playerState.update {
					it.copy(
						isPlaying = false,
						isEnded = true,
						currentPosition = duration,
						duration = duration
					)
				}

				onProgress?.invoke(
					_playerState.value
				)

				onPlaybackEnded?.invoke()
			}


			MediaPlayer.Event.EncounteredError -> {

				Log.e(
					TAG, "VLC playback error"
				)


				_playerState.update {

					it.copy(

						isPlaying = false,

						isBuffering = false,

						error = "VLC failed to play this media"
					)
				}
			}
		}
	}


	// ---------------------------------------------------------
	// RELEASE
	// ---------------------------------------------------------

	fun release() {

		updatePlayerPosition()

		progressJob?.cancel()

		// Final database save
		onProgress?.invoke(
			_playerState.value
		)

		scope.cancel()

		player.stop()

		player.release()

		libVLC.release()
	}

	private suspend fun resolveMediaUri(
		videoUri: String
	): Uri? {

		val uri = Uri.parse(videoUri)

		return when (uri.scheme) {

			"file" -> {
				uri
			}

			"content" -> {

				try {

					val fileName =
						uri.lastPathSegment
							?.substringAfterLast("/")
							?.takeIf { it.isNotBlank() }
							?: "media_${System.currentTimeMillis()}.mkv"

					val safeName =
						fileName
							.replace(Regex("[^a-zA-Z0-9._-]"), "_")

					val cacheFile =
						File(
							appContext.cacheDir,
							"vlc_$safeName"
						)

					if (!cacheFile.exists()) {

						appContext.contentResolver
							.openInputStream(uri)
							?.use { input ->

								cacheFile.outputStream()
									.use { output ->

										input.copyTo(output)
									}
							}
							?: return null
					}

					Uri.fromFile(cacheFile)

				} catch (e: Exception) {

					Log.e(
						TAG,
						"Failed to copy content URI for VLC",
						e
					)

					null
				}
			}

			else -> {

				uri
			}
		}
	}

	fun reset() {

		progressJob?.cancel()

		player.stop()

		_audioTracks.value = emptyList()
		_subtitleTracks.value = emptyList()

		_playerState.value = PlayerState()

		onProgress = null
		onPlaybackEnded = null
	}
}