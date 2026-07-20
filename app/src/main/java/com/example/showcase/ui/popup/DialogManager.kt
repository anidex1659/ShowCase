package com.example.showcase.ui.popup

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object DialogManager {

    private val _dialog =
        MutableStateFlow<GlobalDialog>(
            GlobalDialog.None
        )

    val dialog: StateFlow<GlobalDialog>
        get() = _dialog

    fun show(
        dialog: GlobalDialog
    ) {
        _dialog.value = dialog
    }

    fun dismiss() {
        _dialog.value = GlobalDialog.None
    }
}