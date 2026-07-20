package com.example.showcase.ui.popup.Dialogs.Ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.showcase.ui.theme.VMocha


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditSeriesMetadataDialog(
    seriesId: Long,
    onDismiss: () -> Unit
) {
    Dialog(
        //onDismissRequest = onDismiss
        {}
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            colors = CardDefaults.cardColors(VMocha.Crust),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),

                ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(2.dp),
                        text = "Edite",
                        fontSize = 25.sp,
                        color = VMocha.Text
                    )


                    IconButton(
                        onClick = {}, modifier = Modifier.align(Alignment.Top)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = VMocha.Red
                        )
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    value = "First Love ", //show title
                    colors = OutlinedTextFieldDefaults.colors(
                        VMocha.Lavender,
                        unfocusedTextColor = VMocha.Blue,
                        unfocusedContainerColor = VMocha.Mantle
                    ),
                    onValueChange = {
                    },
                    label = {
                        Text(
                            "Title",
                            color = VMocha.Text
                        )
                    })


                Spacer(modifier = Modifier.padding(10.dp))


                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    value = "First Love ", //show OG title
                    colors = OutlinedTextFieldDefaults.colors(
                        VMocha.Lavender,
                        unfocusedTextColor = VMocha.Blue,
                        unfocusedContainerColor = VMocha.Mantle
                    ),
                    onValueChange = {
                    },
                    label = {
                        Text(
                            "OG title",
                            color = VMocha.Text
                        )
                    })


                Spacer(modifier = Modifier.padding(5.dp))


                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    value = "Plot  ", //show title
                    colors = OutlinedTextFieldDefaults.colors(
                        VMocha.Lavender,
                        unfocusedTextColor = VMocha.Blue,
                        unfocusedContainerColor = VMocha.Mantle
                    ),
                    onValueChange = {
                    },
                    label = {
                        Text(
                            "OG_Title",
                            color = VMocha.Text
                        )
                    })


                Spacer(modifier = Modifier.padding(10.dp))

                Row(

                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        value = "2024", //show title
                        colors = OutlinedTextFieldDefaults.colors(
                            VMocha.Lavender,
                            unfocusedTextColor = VMocha.Blue,
                            unfocusedContainerColor = VMocha.Mantle
                        ),
                        onValueChange = {
                        },
                        label = {
                            Text(
                                "Year",
                                color = VMocha.Text
                            )
                        })


                    Spacer(modifier = Modifier.padding(10.dp))



                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        value = "tty2394", //show Tmdb ID
                        colors = OutlinedTextFieldDefaults.colors(
                            VMocha.Lavender,
                            unfocusedTextColor = VMocha.Blue,
                            unfocusedContainerColor = VMocha.Mantle
                        ),
                        onValueChange = {
                        },
                        label = {
                            Text(
                                "Tmdb ID",
                                color = VMocha.Text
                            )
                        })
                }



                Spacer(modifier = Modifier.padding(10.dp))


                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(VMocha.Mantle),
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 50.dp)
                        .align(Alignment.CenterHorizontally)
                ) {

                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        tint = VMocha.Green
                    )
                }

            }
        }
    }
}
