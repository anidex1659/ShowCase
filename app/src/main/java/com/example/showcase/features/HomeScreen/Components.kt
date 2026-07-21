package com.example.showcase.features.HomeScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.core.progresmanager.ScanProgress
import com.example.showcase.features.HomeScreen.uistate.HomeUiState
import com.example.showcase.features.InfoPage.MoviePosterCard2
import com.example.showcase.features.InfoPage.PosterCard2
import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.ui.navigation.HomeRoute
import com.example.showcase.ui.navigation.LibraryRoute
import com.example.showcase.ui.navigation.MediaRoute
import com.example.showcase.ui.navigation.MovieInfoRoute
import com.example.showcase.ui.navigation.PlayerRoute
import com.example.showcase.ui.navigation.PlayerType
import com.example.showcase.ui.navigation.SeriesInfoRoute
import com.example.showcase.ui.theme.VMocha

//@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel(
    featured: List<SeriesUiModel>,
    navController: NavHostController
) {
    val pagerState = rememberPagerState { featured.size }

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

@Preview(showBackground = true)
@Composable
fun PosterCard() {

    Box(
        modifier = Modifier
            .width(150.dp)
            .height(225.dp)
            .clip(RoundedCornerShape(18.dp, 18.dp, 18.dp, 18.dp))
            .border(
                2.dp,
                VMocha.Blue.copy(alpha = 0.9f),
                RoundedCornerShape(18.dp, 18.dp, 18.dp, 18.dp)
            )
    ) {

        Image(
            painter = painterResource(R.drawable.folder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier
                .fillMaxWidth(0.8f)
                .background(VMocha.Surface1, shape = RoundedCornerShape(0.dp, 18.dp, 18.dp, 0.dp))
                .align(Alignment.BottomStart)
                .border(
                    1.dp,
                    VMocha.Overlay0.copy(alpha = 0.9f),
                    RoundedCornerShape(0.dp, 18.dp, 18.dp, 0.dp)
                )
        ) {

            Text(
                text = "First Love",
                color = VMocha.Text,
                modifier = Modifier
                    .padding(12.dp)
            )
        }

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



//@Preview
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
//            item {
//                MediaSection("Continue Watching") {
//                    PosterRow(uiState.continueWatching)
//                }
//            }
//
                item {
                    MediaSection(
                        "Trending Series",
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
                        "Trending Movies",
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = VMocha.Base,
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
                onClick = {

                }
            ) {

                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = VMocha.Text,
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
                onClick = {

                }
            ) {

                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = VMocha.Text,
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


