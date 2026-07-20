package com.example.showcase.ui.popup.Dialogs.Ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.features.MetaData.data.database.ShowcaseDatabase
import com.example.showcase.ui.popup.Dialogs.Repository.ArtworkRepository
import com.example.showcase.ui.popup.Dialogs.ViewModel.EditArtworkViewModel
import com.example.showcase.ui.popup.Dialogs.ViewModel.EditArtworkViewModelFactory
import com.example.showcase.ui.theme.VMocha


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun EditArtworkDialog(
//
// seriesId: Long,
//    onDismiss: () -> Unit
//
//) {
//
//    val context =
//        LocalContext.current
//
//    val dao = ShowcaseDatabase
//            .getDatabase(context)
//            .showcaseDao()
//
//    val vm: EditArtworkViewModel =
//        viewModel(
//
//            factory =
//                EditArtworkViewModelFactory(
//
//                    dao = dao,
//
//                    repository =
//                        ArtworkRepository()
//                )
//        )
//
//   val artworks by
//    vm.artworks.collectAsState()
//
//    Dialog(
//        //onDismissRequest = onDismiss
//        {}) {
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(500.dp),
//            colors = CardDefaults.cardColors(VMocha.Crust),
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(5.dp),
//
//                ) {
//
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth(0.9f)
//                            .padding(2.dp),
//                        text = "Edite Images",
//                        fontSize = 25.sp,
//                        color = VMocha.Text
//                    )
//
//                    IconButton(
//                        onClick = {}, modifier = Modifier.align(Alignment.Top)
//                    ) {
//
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = null,
//                            tint = VMocha.Red
//                        )
//                    }
//                }
//                LazyVerticalStaggeredGrid(
//
//                    columns = StaggeredGridCells.Fixed(2),
//
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp)
//
//                ) {
//
//                    items(
//                        artworks.size
//                    ) { index ->
//
//                        val artwork =
//                            artworks[index]
//
//                        ArtworkCard(
//                            title = artwork.title,
//                            uri = artwork.uri
//                        )
//                    }
//                }
//            }
//
//        }
//    }
//}
//
//
//@Composable
//fun ArtworkCard(
//    title: String,
//    uri: String?,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {}
//) {
//
//    Column(
//        modifier = modifier.padding(4.dp)
//    ) {
//
//        Card(
//            onClick = onClick,
//            colors = CardDefaults.cardColors(
//                VMocha.Surface0
//            )
//        ) {
//
//            AsyncImage(
//                model = uri,
//                contentDescription = title,
//                modifier = Modifier.fillMaxWidth(),
//                contentScale = ContentScale.FillWidth
//            )
//        }
//
//        Text(
//            text = title,
//            color = VMocha.Text,
//            modifier = Modifier.padding(top = 4.dp)
//        )
//    }
//}