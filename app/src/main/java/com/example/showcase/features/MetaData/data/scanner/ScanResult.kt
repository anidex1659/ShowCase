package com.example.showcase.features.MetaData.data.scanner

data class ScanResult(   //used For Siress

    val seriesTitle: String,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val filePath: String,
    val folderPath: String

)



data class MovieScanResult(

    val title: String,
    val year: Int?,
    val filePath: String,

    val folderPath: String
)



data class MusicScanResult(

    val title: String,

    val artist: String?,

    val album: String?,

    val filePath: String,

    val folderPath: String
)