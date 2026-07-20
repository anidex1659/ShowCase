package com.example.showcase.features.Library.ui.Components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.showcase.features.MetaData.data.database.DatabaseProvider
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.launch

@Composable
fun AddLibraryDialog(
    showDialog: Boolean,

    onDismiss: () -> Unit
) {

    if (!showDialog)
        return

    Dialog(
        onDismissRequest = onDismiss
    ) {
        AddLibraryDialogContent(onDismiss)
    }
}

@Composable
fun AddLibraryDialogContent(onDismiss: () -> Unit) {
    var libraryName by remember {
        mutableStateOf("")
    }

    var folderUri by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf("Series")
    }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val folderPicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.OpenDocumentTree()
        ) { uri ->

            uri ?: return@rememberLauncherForActivityResult

            context.contentResolver
                .takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

            folderUri =
                uri.toString()
        }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(VMocha.Surface0)
    ) {

        Column(

            modifier = Modifier.padding(24.dp),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Add Library",
                color = VMocha.Text
            )

            OutlinedTextField(

                value = libraryName,

                onValueChange = {
                    libraryName = it
                },

                label = {
                    Text(
                        "Library Name",
                        color = VMocha.Text
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VMocha.Lavender,
                    unfocusedBorderColor = VMocha.Blue,
                    focusedTextColor = VMocha.Text,
                    unfocusedTextColor = VMocha.Text,
                    cursorColor = VMocha.Lavender
                )
            )
            Column {

                Text(
                    text = "Library Type",
                    color = VMocha.Text
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    listOf(
                        "Series",
                        "Movies",
                        "Music"
                    ).forEach { type ->

                        FilterChip(
                            colors = FilterChipDefaults.filterChipColors(selectedLabelColor = VMocha.Blue),

                            selected =
                                selectedType == type,

                            onClick = {
                                selectedType = type
                            },

                            label = {
                                Text(
                                    type,
                                    color = VMocha.Text
                                )
                            }
                        )
                    }
                }
            }

            if (folderUri.isNotBlank()) {

                Text(
                    text = folderUri,
                    color = VMocha.Subtext0
                )
            }

            Button(

                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(VMocha.Lavender),
                shape = RoundedCornerShape(20),
                onClick = { folderPicker.launch(null) }) {
                Text("Select Folder", color = VMocha.Text)
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(VMocha.Lavender),
                shape = RoundedCornerShape(20),
                onClick = {
                    scope.launch {

                        val dao =
                            DatabaseProvider
                                .getDatabase(context)
                                .dao()

                        dao.insertLibrary(
                            LibraryEntity(
                                name = libraryName,
                                type = selectedType,
                                folderUri = folderUri,
                                enabled = true,
                                createdDate = System.currentTimeMillis(),
                                lastScan = 0
                            )
                        )

                        onDismiss()
                    }
                }
            ) {
                Text("Save", color = VMocha.Text)
            }
        }
    }
}