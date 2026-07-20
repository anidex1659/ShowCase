package com.example.showcase.core.progresmanager


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ProgressManager {

    private val _progress =
        MutableStateFlow(
            ScanProgress()
        )

    val progress: StateFlow<ScanProgress> =
        _progress.asStateFlow()

    fun update(

        title: String,

        subtitle: String,

        current: Int,

        total: Int

    ) {

        _progress.value = ScanProgress(

            visible = true,

            title = title,

            subtitle = subtitle,

            current = current,

            total = total,

            progress =
                if (total == 0)
                    0f
                else
                    current.toFloat() / total
        )
    }

    fun show(
        title: String,
        subtitle: String = ""
    ) {

        _progress.value = ScanProgress(

            visible = true,

            title = title,

            subtitle = subtitle
        )
    }

    fun hide() {

        _progress.value =
            ScanProgress()
    }
}