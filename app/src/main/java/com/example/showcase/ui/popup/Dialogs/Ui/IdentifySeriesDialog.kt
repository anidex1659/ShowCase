package com.example.showcase.ui.popup.Dialogs.Ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.features.MetaData.data.database.DatabaseProvider
import com.example.showcase.features.MetaData.data.remote.TmdbNetworkModule
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.remote.model.TmdbSeriesResult
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.MetaData.data.repository.metadata.sires.MetadataRepository
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.Player.model.storage.ArtworkStorageManager
import com.example.showcase.features.Player.model.storage.MetadataJsonManager

import com.example.showcase.features.Player.model.storage.MovieMetadataJsonManager
import com.example.showcase.ui.popup.Dialogs.Repository.IdentifySeriesRepository
import com.example.showcase.ui.popup.Dialogs.ViewModel.IdentifySeriesViewModel
import com.example.showcase.ui.popup.Dialogs.ViewModel.IdentifySeriesViewModelFactory
import com.example.showcase.ui.theme.VMocha

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun IdentifySeriesDialog(
    seriesId: Long,
    onDismiss: () -> Unit
) {
    val context =
        LocalContext.current

    val dao =
        DatabaseProvider
            .getDatabase(context)
            .dao()
    val remote =
        TmdbRemoteDataSource(
            TmdbNetworkModule.tmdbApi
        )
    val metadataRepository =
        MetadataRepository(
            remote = remote
        )
    val metadataStorageRepository =
        MetadataStorageRepository(
            context = context,
            jsonManager = MetadataJsonManager(dao),
            movieJsonManager = MovieMetadataJsonManager(dao)
        )
    val artworkDownloader =
        ArtworkDownloader(

            context = context,

            storageManager =
                ArtworkStorageManager(),
                        dao=dao
        )

    val repository =
        IdentifySeriesRepository(

            dao = dao,

            metadataRepository =
                metadataRepository,

            metadataStorageRepository =
                metadataStorageRepository,

            artworkDownloader =
                artworkDownloader,

            remote = remote
        )

    val viewModel: IdentifySeriesViewModel = viewModel(
        factory = IdentifySeriesViewModelFactory(
            remote = remote,
            repository = repository
        )
    )

    val state by viewModel.uiState.collectAsState()
    Dialog(
        onDismissRequest = onDismiss
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
                        text ="Identify",
                        fontSize = 25.sp,
                        color = VMocha.Text
                    )


                    IconButton(
                        onClick = {}, modifier = Modifier.align(Alignment.Top)) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = VMocha.Red
                        )
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title, //show title
                    colors = OutlinedTextFieldDefaults.colors(
                        VMocha.Lavender,
                        unfocusedTextColor = VMocha.Blue
                    ),
                    onValueChange = {
                        viewModel.updateTitle(it)
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
                    value = state.tmdbId?.toString() ?: "", //show Tmdb_id
                    colors = OutlinedTextFieldDefaults.colors(
                        VMocha.Lavender,
                        unfocusedTextColor = VMocha.Blue
                    ),
                    onValueChange = {},
                    label = {
                        Text(
                            "Tmdb_ID",
                            color = VMocha.Text
                        )
                    })


                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(
                        onClick = {
                            viewModel.search()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = VMocha.Blue
                        )
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.tmdbId != null,
                        onClick = {

                            viewModel.identifySeries(
                                seriesId = seriesId
                            )

                            onDismiss()
                        }
                    ) {

                        Text("Apply Metadata")
                    }
                }


//                Spacer(modifier = Modifier.padding(5.dp))
//
//
//                IconButton(
//                    onClick = {
//
//                        viewModel.search()
//                    }
//                    , modifier = Modifier.align(Alignment.End)) {
//
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = null,
//                        tint = VMocha.Blue
//                    )
//                }

                if (state.loading) {

                    Text(
                        text = "Searching...",
                        color = VMocha.Text
                    )
                }

                SearchResults(
                    results = state.results,
                    selectedId = state.tmdbId,
                    onSelect = {
                        viewModel.selectResult(it)
                    }
                )

            }
        }
        
    }
}


@Composable
fun SearchResults(
results: List<TmdbSeriesResult>,

selectedId: Int?,

onSelect: (TmdbSeriesResult) -> Unit
) {

    LazyColumn {

        items(
            results
        ) { result ->

            ResultCard(

                result = result,

                selected =
                    result.id == selectedId,

                onClick = {
                    onSelect(result)
                }
            )
        }
    }
}



@Composable
private fun ResultCard( result: TmdbSeriesResult,selected: Boolean, onClick: () -> Unit) {
      val IMAGE_BASE =
        "https://image.tmdb.org/t/p/w500"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(

            if (selected)
                VMocha.Blue.copy(alpha = 0.25f)
            else
                VMocha.Base
        )
    )
    {
        Row(

        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(18.dp, 18.dp, 18.dp, 18.dp))
                    .border(
                        2.dp,
                        VMocha.Blue.copy(alpha = 0.9f),
                        RoundedCornerShape(18.dp, 18.dp, 18.dp, 18.dp)
                    )
            ) {

                AsyncImage(
                    model =
                        result.posterPath?.let {
                            "$IMAGE_BASE$it"
                        },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(Modifier.padding(10.dp))

            Column() {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(2.dp),
                    text = result.name,
                    fontSize = 25.sp,
                    color = VMocha.Text
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(2.dp),
                    text=result.firstAirDate,
                    fontSize = 15.sp,
                    color = VMocha.Text
                )
            }

        }

    }
}