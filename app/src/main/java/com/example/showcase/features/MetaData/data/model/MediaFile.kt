package com.example.showcase.features.MetaData.data.model

import android.net.Uri

data class MediaFile(
    val name: String,
    val uri: Uri,
    val parentFolder: String?,
    val parentFolderName: String?
)