package com.example.showcase.features.HomeScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.HomeScreen.repo.HomeRepository
import com.example.showcase.features.HomeScreen.viewmodel.HomeViewModel
import com.example.showcase.features.HomeScreen.viewmodel.HomeViewModelFactory
import com.example.showcase.features.MetaData.data.database.DatabaseProvider
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.storage.MetadataJsonManager
import com.example.showcase.features.Player.model.storage.MovieMetadataJsonManager
import com.example.showcase.features.Player.repository.PlayerRepository
import com.example.showcase.features.SplashScreen.SplashScreenImg
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun HomeScreen(navController: NavHostController
    ) {

    val context =
        LocalContext.current

    val progress by ProgressManager
        .progress
        .collectAsState()

    val dao =
        DatabaseProvider
            .getDatabase(context)
            .dao()


    val seriesRepository = remember { SeriesRepository(dao) }
    val movieRepository = remember { MovieRepository(dao) }


    val metadataStorageRepository: MetadataStorageRepository = MetadataStorageRepository(
        context = context,
        jsonManager = MetadataJsonManager(dao),
        movieJsonManager = MovieMetadataJsonManager(dao)
    )

    val playerrepository = PlayerRepository(seriesRepository, movieRepository, dao,metadataStorageRepository)

    PlayerGraph.initialize(
        context = context,
        repository = playerrepository
    )

    val repository = remember {
        HomeRepository(seriesRepository,movieRepository)
    }

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val playerViewModel = remember {

        PlayerViewModel(
            PlayerGraph.playerManager,
            PlayerGraph.repository
        )
    }

    val uiState by viewModel.uiState.collectAsState()


    val backgrounds = remember(uiState) {

        buildList {

            // Series backdrops
            addAll(
                uiState.hero.mapNotNull { it.poster?.uri }
            )

            addAll(
                uiState.trendingSeries.mapNotNull { it.poster?.uri }
            )

            addAll(
                uiState.recentlyAddedSeries.mapNotNull { it.poster?.uri }
            )

            // Movie backdrops
            addAll(
                uiState.trendingMovies.mapNotNull {
                    it.artwork.firstOrNull { art ->
                        art.type == "poster"
                    }?.uri
                }
            )

            addAll(
                uiState.recentlyAddedMovies.mapNotNull {
                    it.artwork.firstOrNull { art ->
                        art.type == "poster"
                    }?.uri
                }
            )
        }.distinct()
    }

    var currentBackground by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(backgrounds) {

        if (backgrounds.isEmpty()) return@LaunchedEffect

        while (true) {

            currentBackground =
                backgrounds.random()

            delay(8000) // every 8 seconds
        }
    }

    var animationFinished by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(3500)
        animationFinished = true
    }

    val ready =
        animationFinished &&
                !uiState.loading


    var refreshing by remember {
        mutableStateOf(false)
    }

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            refreshing = true

            viewModel.refresh()

            refreshing = false
        }
    ) {

        Box() {

            Box(modifier = Modifier.fillMaxSize())
            {
                AnimatedContent(
                    targetState = currentBackground,
                    transitionSpec = {

                        fadeIn(
                            animationSpec = tween(1200)
                        ) togetherWith

                                fadeOut(
                                    animationSpec = tween(1200)
                                )
                    },
                    label = ""
                ) { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = "home_screen_BackGround",
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(1.dp),
                        contentScale = ContentScale.Crop,

                        )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = VMocha.Lavender.copy(0.2f)
                        )
                )

                //^^ Background Image

                Column(
                    Modifier.fillMaxSize()
                )
                {
                    Spacer(modifier = Modifier.height(40.dp))
                    HeroCarousel(
                        featured = uiState.hero, navController = navController,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Column() {
                        ContinueButton()
                        //^^contnuew button

                        Spacer(modifier = Modifier.height(10.dp))
                        Panel(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 20.dp
                                        )
                                    )
                                    .background(color = VMocha.Mantle.copy(0.8f)),
                            uiState = uiState,
                            navController, progress, playerViewModel
                        )

                    }


                }


            }

            Crossfade(
                targetState = ready
            ) { ready ->
                if (!ready) {
                    SplashScreenImg()
                }
            }


        }

    }

}


//Continiew Watching Button
//@Preview
@Composable
fun ContinueButton() {

    var expanded by remember {
        mutableStateOf(false)
    }

    // Demo animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            expanded = true

            delay(3000)
            expanded = false

            delay(40000)
        }
    }

    val width by animateDpAsState(
        targetValue = if (expanded) 220.dp else 58.dp,
        animationSpec = tween(500),
        label = ""
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {

        Button(
            onClick = {},
            modifier = Modifier
                .width(width)
                .height(50.dp),   // Fixed height
            shape = RoundedCornerShape(
                topStart = 10.dp,
                bottomStart = 10.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = VMocha.Lavender
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = VMocha.Text
                )

                AnimatedVisibility(expanded) {

                    Row {

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "Continue Watching",
                            color = VMocha.Text,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

