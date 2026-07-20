package com.example.showcase.core.progresmanager

data class ScanProgress(

    val visible: Boolean = false,

    val title: String = "",

    val subtitle: String = "",

    val progress: Float = 0f,

    val current: Int = 0,

    val total: Int = 0
)