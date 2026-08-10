package com.example.showcase.features.HomeScreen.ui

import android.text.Layout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.core.ProgressManager.ProgressManager
import com.example.showcase.core.ProgressManager.ProgressType
import com.example.showcase.core.ProgressManager.ScanProgress
import com.example.showcase.features.HomeScreen.model.HomeUiState
import com.example.showcase.features.InfoPage.ui.MoviePosterCard2
import com.example.showcase.features.InfoPage.ui.PosterCard2
import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.features.Player.PlayerCore.PlayerManager
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.model.MediaType
import com.example.showcase.features.Player.ui.VlcVideoPlayer
import com.example.showcase.ui.Navigation.HomeRoute
import com.example.showcase.ui.Navigation.LibraryRoute
import com.example.showcase.ui.Navigation.MediaRoute
import com.example.showcase.ui.Navigation.MovieInfoRoute
import com.example.showcase.ui.Navigation.PlayerGraph
import com.example.showcase.ui.Navigation.PlayerRoute
import com.example.showcase.ui.Navigation.PlayerType
import com.example.showcase.ui.Navigation.SeriesInfoRoute
import com.example.showcase.ui.Navigation.SettingsRoute
import com.example.showcase.ui.Theme.VMocha
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel(
    featured: List<SeriesUiModel>,
    navController: NavHostController
) {
    if (featured.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { featured.size }
    )

    // Auto-slide every 5 seconds
    LaunchedEffect(pagerState, featured.size) {
        while (true) {
            delay(5000L)

            val nextPage =
                (pagerState.currentPage + 1) % featured.size

            pagerState.animateScrollToPage(
                page = nextPage
            )
        }
    }

    HorizontalPager(
        state = pagerState
    ) { page ->

        HeroCard(
            series = featured[page],
            onClick = {
                navController.navigate(
                    SeriesInfoRoute(
                        featured[page].series.id
                    )
                )
            }
        )
    }
}


@Composable
fun HeroCard(
    onClick: () -> Unit, series: SeriesUiModel
) {


    Column {

        Card(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(start = 20.dp, end = 20.dp)
                .border(1.dp, VMocha.Blue, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            AsyncImage(
               model = series.backdrop?.uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClick() },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if(series.logo == null){
            Text(series.series.title)
        }
        AsyncImage(
            model = series.logo?.uri,
            contentDescription = "Series Logo",
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            contentScale = ContentScale.Fit
        )

    }
}
@Composable
fun PosterRow(
    series: List<SeriesUiModel>,
    navController: NavHostController,
    playerViewModel: PlayerViewModel
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        items(series.size) { index ->

            PosterCard2(
                series[index],
                {
                navController.navigate(
                    SeriesInfoRoute(
                        series[index].series.id
                    )
                )
            },
                {

                    navController.navigate(
                        PlayerRoute(id = series[index].series.id, type = PlayerType.SERIES)
                    )
                }
            )

        }
    }
}

@Composable
fun MoviePosterRow(
    movies: List<MovieUiModel>,
    navController: NavHostController,
    playerViewModel: PlayerViewModel
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        items(movies.size) { index ->

            MoviePosterCard2(
                movies[index],
                {
                navController.navigate(
                    MovieInfoRoute(
                        movies[index].movie.id
                    )
                )
            }, {
                playerViewModel.startMovie(movies[index].movie.id)
                navController.navigate(
                    route = PlayerRoute(id=movies[index].movie.id, type = PlayerType.MOVIE))
                }
                )
        }
    }
}

@Composable
fun Panel(
    modifier: Modifier,
    uiState: HomeUiState,
    navController: NavHostController,
    progress: ScanProgress,
    playerViewModel: PlayerViewModel
) {  //home panel
    Box(
        modifier = modifier
    ) {
        Column() {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {

                item {
                    MediaSection(
                        "Series",
                        onClick = {
                            navController.navigate(MediaRoute("Series"))
                        }
                    ) {
                        PosterRow(
                            uiState.trendingSeries,
                            navController,
                            playerViewModel
                        )
                    }
                }

                item {
                    MediaSection(
                        "Movies",
                        onClick = {
                            navController.navigate(MediaRoute("Movies"))
                        }
                    ) {
                        MoviePosterRow(
                            uiState.trendingMovies,
                            navController,
                            playerViewModel
                        )
                    }
                }


            }

            BootomBAR(navController, progress)
        }

    }

}

@Composable
fun MediaSection(     //singel row
    title: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit,

) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = VMocha.Text,
        modifier = Modifier
            .padding(12.dp)
            .clickable(onClick = onClick)
    )

    content()
}

//@Preview
@Composable
fun BootomBAR(
    navController: NavHostController,
    progress: ScanProgress,
    modifier: Modifier = Modifier
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = VMocha.Crust,
                shape = RoundedCornerShape(
                    0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            ),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {navController.navigate(HomeRoute)}) {

            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null, tint = VMocha.Text
            )
        }
        var poster by remember { mutableStateOf(progress.playerState?.media?.backdrop) }
        AnimatedVisibility(
            visible = progress.visible && progress.type == ProgressType.PLAYING,
            enter = expandIn(),
            exit = fadeOut()
        ) {
          poster=progress.playerState?.media?.backdrop
            Box(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(top = 1.dp, bottom = 1.dp)
            ) {
                AsyncImage(
                    model = poster,
                    contentDescription = "",
                    Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .background(VMocha.Crust.copy(0.5f))
                )
                Text(
                    progress.playerState?.media?.title?:"",
                    fontSize = 10.sp,
                    color = VMocha.Subtext0,
                    modifier = modifier.align(Alignment.Center)
                )

                Row(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .align(Alignment.Center)
                ) {//mini player controllers
                    IconButton(
                        onClick = {
                            if (progress.playerState?.media?.type == MediaType.MOVIE)
                                navController.navigate(
                                    PlayerRoute(
                                        progress.playerState.media.id,
                                        PlayerType.MOVIE
                                    )
                                )
                        },
                        enabled = if (progress.playerState?.media?.type == MediaType.MOVIE) true else false
                    ) {
                        Icon(
                            imageVector = Icons.Default.SubdirectoryArrowRight,
                            contentDescription = null,
                            tint = VMocha.Text
                        )
                    }
                    IconButton(
                        onClick = {
                            if (progress.playerState?.isPlaying?:true) {
                                PlayerGraph.playerManager.pause()
                                PlayerGraph.vlcPlayerManager.pause()

                            }else{
                                PlayerGraph.playerManager.play()
                                PlayerGraph.vlcPlayerManager.play()
                            }


                        }
                    ) {
                        if (progress.playerState?.isPlaying ?: true)
                            Icon(
                                imageVector = Icons.Default.PauseCircle,
                                contentDescription = null,
                                tint = VMocha.Text
                            )
                        else
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = VMocha.Text
                            )

                    }

                    IconButton(
                        onClick = {
                            PlayerGraph.playerManager.reset()
                            PlayerGraph.vlcPlayerManager.reset()}
                    ) {

                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = null,
                            tint = VMocha.Red
                        )
                    }
                }
            }
        }


        Row {

            IconButton(
                onClick = {navController.navigate(LibraryRoute)}
            ) {

                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }
            IconButton(
                onClick = {navController.navigate(SettingsRoute)}
            ) {

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }
        }
    }

}

@Composable
fun TopBar(
    navController: NavHostController,
    progress: ScanProgress,
    modifier: Modifier = Modifier
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
                color = VMocha.Base,
                shape = RoundedCornerShape(
                    20.dp
                )
            ),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {navController.navigate(HomeRoute)}) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null, tint = VMocha.Text
            )
        }

        Row {
            IconButton(
                onClick = {navController.navigate(LibraryRoute)}
            ) {

                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }
            IconButton(
                onClick = {}
            ) {

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = VMocha.Text
                )
            }
        }
    }

}


