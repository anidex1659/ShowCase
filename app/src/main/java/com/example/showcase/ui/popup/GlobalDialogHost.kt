package com.example.showcase.ui.popup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
//import com.example.showcase.ui.popup.Dialogs.Ui.EditArtworkDialog
import com.example.showcase.ui.popup.Dialogs.Ui.EditSeriesMetadataDialog
import com.example.showcase.ui.popup.Dialogs.Ui.IdentifySeriesDialog

@Composable
fun GlobalDialogHost() {

    val dialog by
    DialogManager.dialog.collectAsState()

    when (val current = dialog) {

        is GlobalDialog.EditArtwork -> {

//            EditArtworkDialog(
//                seriesId = current.seriesId,
//                onDismiss = {
//                    DialogManager.dismiss()
//                }
//            )
        }

        is GlobalDialog.EditSeriesMetadata -> {

            EditSeriesMetadataDialog(
                seriesId = current.seriesId,
                onDismiss = {
                    DialogManager.dismiss()
                }
            )
        }

        is GlobalDialog.IdentifySeries -> {

            IdentifySeriesDialog(
                seriesId = current.seriesId,
                onDismiss = {
                    DialogManager.dismiss()
                }
            )
        }

        GlobalDialog.None -> {}
    }
}