package com.example.showcase.features.Library.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.core.progresmanager.ScanProgress
import com.example.showcase.features.Library.repository.LibraryRepository

//import com.example.showcase.features.MetaData.data.repository.LibraryRepository
import com.example.showcase.features.Library.ui.Components.AddLibraryDialog
import com.example.showcase.features.Library.ui.Components.LibraryPanel
import com.example.showcase.features.Library.viewmodel.LibraryViewModel
import com.example.showcase.features.Library.viewmodel.LibraryViewModelFactory
import com.example.showcase.features.MetaData.data.database.DatabaseProvider
import com.example.showcase.features.MetaData.data.remote.TmdbNetworkModule
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.features.MetaData.data.repository.Artwork.ArtworkDownloader
import com.example.showcase.features.MetaData.data.repository.metadata.sires.MetadataRepository
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository
import com.example.showcase.features.MetaData.data.scanner.Detectors.MovieDetector
import com.example.showcase.features.MetaData.data.scanner.Detectors.MusicDetector
import com.example.showcase.features.MetaData.data.scanner.Scanners.LibraryScanner
import com.example.showcase.features.MetaData.data.scanner.Scanners.MovieScanner
import com.example.showcase.features.MetaData.data.scanner.Scanners.MusicScanner
import com.example.showcase.features.MetaData.data.scanner.ScannerManager
import com.example.showcase.features.MetaData.data.scanner.Detectors.SeriesDetector
import com.example.showcase.features.Player.model.storage.ArtworkStorageManager
import com.example.showcase.features.Player.model.storage.MetadataJsonManager
import com.example.showcase.features.Player.model.storage.MovieMetadataJsonManager
import com.example.showcase.features.SplashScreen.LoadingPopup
import com.example.showcase.ui.popup.GlobalDialogHost
import com.example.showcase.ui.theme.VMocha


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LibraryScreen() {

    GlobalDialogHost()

    val context =
        LocalContext.current


    val progress by ProgressManager
        .progress
        .collectAsState()

    val dao =
        DatabaseProvider
            .getDatabase(context)
            .dao()


    val seriesDetector = SeriesDetector()

    val movieDetector = MovieDetector()

    val musicDetector = MusicDetector()


    val metadataRepository =

        MetadataRepository(

            remote =
                TmdbRemoteDataSource(
                    TmdbNetworkModule.tmdbApi
                )
        )

    val metadataStorageRepository =
        MetadataStorageRepository(
            context = context,
            jsonManager = MetadataJsonManager(dao),
            movieJsonManager = MovieMetadataJsonManager(dao)
        )

    val remote =

        TmdbRemoteDataSource(
            TmdbNetworkModule.tmdbApi
        )

    val artworkStorageManager = ArtworkStorageManager()

    val artworkDownloader =

        ArtworkDownloader(
            context = context,
            artworkStorageManager,
            dao = dao
        )

    val libraryScanner =

        LibraryScanner(

            detector = seriesDetector,

            metadataRepository = metadataRepository,

            metadataStorageRepository = metadataStorageRepository,

            dao = dao,

            artworkDownloader = artworkDownloader,

            remote = remote
        )


    val movieScanner =

        MovieScanner(

            detector = movieDetector,

            dao = dao,

            metadataRepository = metadataRepository,

            metadataStorageRepository = metadataStorageRepository,

            artworkDownloader = artworkDownloader,

            remote = remote
        )
    val seriesRepository =

        SeriesRepository(
            dao
        )

    val movieRepository =

        MovieRepository(
            dao
        )

    val musicScanner =

        MusicScanner(

            detector = musicDetector,

            dao = dao
        )


    val scannerManager =

        ScannerManager(

            dao = dao,

            libraryScanner = libraryScanner,

            movieScanner = movieScanner,

            musicScanner = musicScanner
        )

    val repository =

        LibraryRepository(

            dao = dao,

            scannerManager = scannerManager,

            seriesRepository = seriesRepository,

            movieRepository = movieRepository

        )

    val viewModel: LibraryViewModel =
        viewModel(
            factory =
                LibraryViewModelFactory(
                    repository
                )
        )

    val uiState by
    viewModel.uiState
        .collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }


    LoadingPopup(progress.visible,progress.title)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VMocha.Crust)
    ) {


        Spacer(
            modifier = Modifier.height(10.dp)
        )

//        Text(
//            text = "Libraries: ${uiState.libraries.size}"
//        )

        LibraryPanel(
            libraries = uiState.libraries,
            onScanLibrary = { library ->

                viewModel.scanLibrary(
                    context = context,
                    libraryName = library.library.name
                )
            },
            progress
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        MenuBar(
            onAddClick = {
                showDialog = true
            },
            onScanClick = {
                viewModel.scanAllLibraries(context)
            }
            ,progress

        )
    }

    AddLibraryDialog(
        showDialog = showDialog, onDismiss = {
            showDialog = false
        })
}


@Composable
fun MenuBar(
    onAddClick: () -> Unit, onScanClick: () -> Unit, progress: ScanProgress,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val rotation = if (progress.visible) {

        infiniteTransition.animateFloat(

            initialValue = 0f,
            targetValue = 360f,

            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),

            label = ""

        ).value

    } else {

        0f
    }


    val tintcolor = if (progress.visible) {
        VMocha.Red
    } else {
        VMocha.Text
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = VMocha.Base, shape = RoundedCornerShape(20.dp)
            ),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {}) {

            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = VMocha.Text
            )
        }

        Column(  modifier = Modifier
            .padding(top=10.dp, bottom = 10.dp)
            .fillMaxWidth(0.7f))
        {
//            Text(
//
//            if (!progress.visible)
//                ""
//            else
//                "${progress.title}",
//
//            textAlign = TextAlign.Start,
//            color = VMocha.Subtext1,
//            fontSize = 20.sp,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//
//        )
//
//            Text(
//
//                if (!progress.visible)
//                    ""
//                else
//                    "<${progress.subtitle}>",
//
//                textAlign = TextAlign.Start,
//                color = VMocha.Subtext0,
//                fontSize = 15.sp,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
            }



        Row {

            IconButton(
                onClick = onScanClick
            ) {

                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = tintcolor,
                    modifier = Modifier.rotate(rotation)
                )
            }

            IconButton(
                onClick = onAddClick
            ) {

                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null, tint = VMocha.Text
                )
            }
        }
    }
}
