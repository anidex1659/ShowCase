package com.example.showcase.features.Player.model

enum class MediaType {
    MOVIE,
    EPISODE
}

data class PlayerMedia(

    val id: Long,

    val type: MediaType,

    val title: String,

    val videoUri: String,

    val poster: String? = null,

    val backdrop: String? = null,

    val seasonNumber: Int? = null,

    val episodeNumber: Int? = null,

    val duration: Long = 0L,
    val resume: Long=0L
)