package com.example.showcase.features.MediaPages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.HomeScreen.BootomBAR
import com.example.showcase.features.HomeScreen.TopBar
import com.example.showcase.features.HomeScreen.repo.HomeRepository
import com.example.showcase.features.HomeScreen.viewmodel.HomeViewModel
import com.example.showcase.features.HomeScreen.viewmodel.HomeViewModelFactory
import com.example.showcase.features.InfoPage.repository.InfoRepository
import com.example.showcase.features.InfoPage.uistate.MovieInfoUiModel
import com.example.showcase.features.InfoPage.uistate.SeriesInfoUiModel
import com.example.showcase.features.InfoPage.viewmodel.InfoViewModel
import com.example.showcase.features.InfoPage.viewmodel.InfoViewModelFactory
import com.example.showcase.features.Library.ui.Components.TypewriterText
import com.example.showcase.features.MetaData.data.database.DatabaseProvider
import com.example.showcase.features.MetaData.data.repository.metadata.MetadataStorageRepository
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository
import com.example.showcase.features.Player.model.storage.MetadataJsonManager
import com.example.showcase.features.Player.model.storage.MovieMetadataJsonManager
import com.example.showcase.ui.navigation.MovieInfoRoute
import com.example.showcase.ui.navigation.PlayerRoute
import com.example.showcase.ui.navigation.PlayerType
import com.example.showcase.ui.navigation.SeriesInfoRoute
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay


//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MediaPage(navController: NavHostController,Show : String) {


    val context =
        LocalContext.current

    val progress by ProgressManager
        .progress
        .collectAsState()

    val dao = DatabaseProvider.getDatabase(context).dao()

    val seriesRepository = remember { SeriesRepository(dao) }
    val movieRepository = remember { MovieRepository(dao) }

    val repository = remember {
        HomeRepository(seriesRepository, movieRepository)
    }

    val jsonmanger= MetadataJsonManager(dao)
    val movieMetadataJsonManager = MovieMetadataJsonManager(dao)

    val MetadataRepository = MetadataStorageRepository(context,jsonmanger,movieMetadataJsonManager)


    val Inforepository = remember {
        InfoRepository(
            dao = dao,
            seriesRepository = SeriesRepository(dao),
            movieRepository = MovieRepository(dao),
            MetadataRepository
        )
    }

    val MediaViewModel: MediaViewModel = viewModel(
        factory = MediaViewModelFactory(repository,Inforepository)
    )
    LaunchedEffect(Show) {

        when(Show){

            "Movies" ->
                MediaViewModel.load(MediaType.MOVIE)

            "Series" ->
                MediaViewModel.load(MediaType.SERIES)
        }
    }
    val uiState by MediaViewModel.uiState.collectAsState()


    val media = uiState.media


    Box(Modifier
        .fillMaxSize()
        .background(VMocha.Crust)) {

        Column() {

            Spacer(Modifier.padding(2.dp))

            HeroCarousel2(media,navController)
            
            Spacer(Modifier.padding(2.dp))

            TopBar(navController,progress)

//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .background(shape = RoundedCornerShape(20.dp), color = VMocha.Crust)
//            )
//            {
//                TypewriterText(
//                    "Movies",
//                    Modifier
//                        .fillMaxSize()
//                        .align(Alignment.Center)
//                        .padding(vertical = 10.dp),
//                    fontSize = 20.sp,
//                    textAlign = TextAlign.Center,
//                    fontWeight = FontWeight.ExtraBold,
//                    color = VMocha.Text
//                )
//            }

            Spacer(Modifier.padding(2.dp))

            LazyColumn {
                    items(media.chunked(2)) { row ->

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        row.forEach { media ->

                            PosterCard3(media = media, onClick = {
                                if (media.type == MediaType.SERIES) {
                                    navController.navigate(SeriesInfoRoute(media.id))
                                } else {
                                    navController.navigate(SeriesInfoRoute(media.id))
                                }
                            }, onPlayClick = {
                                    if (media.type == MediaType.SERIES) {
                                        navController.navigate(
                                            PlayerRoute(
                                                media.id,
                                                type = PlayerType.SERIES
                                            )
                                        )
                                    } else {
                                        navController.navigate(
                                            PlayerRoute(
                                                media.id,
                                                type = PlayerType.MOVIE
                                            )
                                        )
                                    }
                                }
                            )
                        }

                        if (row.size == 1) {
                            Spacer(Modifier.width(150.dp))
                        }
                    }
                }
            }



        }


    }

}


@Composable
fun AnimatedBackdrop(duraction: Int, modifier: Modifier, image: Any?) {

    val transition = rememberInfiniteTransition()

    val scale by transition.animateFloat(
        1f,
        1.1f,
        infiniteRepeatable(
            tween(18000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    val offsetX by transition.animateFloat(
        0f,
        40f,
        infiniteRepeatable(
            tween(18000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    val offsetY by transition.animateFloat(
        0f,
        20f,
        infiniteRepeatable(
            tween(18000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )
    Box(modifier) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX
                    translationY = offsetY
                },
            contentScale = ContentScale.Crop
        )
    }
}


//@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PosterCard3(media: MediaCardUiModel, onClick: () -> Unit, onPlayClick: () -> Unit) {
    val scale by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 1f, targetValue = 1.08f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200, easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val poster = media.poster

    Column() {

        Card(
            shape = RoundedCornerShape(20, 20, 20, 20), colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            //border = BorderStroke(width = 5.dp, color = VMocha.Blue),
            modifier = Modifier
                //.padding(16.dp, 16.dp, 0.dp, 0.dp)
                .height(225.dp)
                .width(150.dp)
                .clickable(
                    onClick = onClick
                )
                .padding(bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    shape = RoundedCornerShape(20, 20, 0, 0),
                    border = BorderStroke(width = 2.dp, color = VMocha.Blue),
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 20.dp)
                        .fillMaxSize()
                ) {

                    AsyncImage(
                        model = poster?.toUri(),
                        contentDescription = "thumb",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )

//                    Image(
//                        painter = painterResource(R.drawable.sifi),//backdroup image tobe changed
//                        contentDescription = "thumb",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//
//                    )


                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .height(40.dp)
                        .fillMaxWidth(0.8f)
                        //.width(100.dp)

                        .background(
                            VMocha.Base, RoundedCornerShape(0)
                        )
                ) {
                    Text(
                        text = media.title, //title
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VMocha.Text,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxSize()
                            //.basicMarquee()
                            .padding(start = 30.dp, top = 10.dp, end = 5.dp),
                        maxLines = 1,
                        textAlign = TextAlign.Center

                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .height(60.dp)
                        .width(60.dp)
                        .background(
                            VMocha.Base, RoundedCornerShape(100)
                        )
                ) {
                    IconButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = onPlayClick
                    ) {
                        Icon(
                            Icons.Default.PlayCircle,
                            null,
                            tint = VMocha.Text,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .graphicsLayer(
                                    scaleX = scale, scaleY = scale
                                )
                        )
                    }
                }

            }


        }
    }
}


//@Preview
@Composable
fun HeroCarousel2(
    media: List<MediaCardUiModel>,
    navController: NavHostController,


    ) {
    val pagerState = rememberPagerState { media.size }
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp)
    ) { page ->

        Herocard(
            onClick = {
                if (media[page].type == MediaType.SERIES) {
                    navController.navigate(route = SeriesInfoRoute(media[page].id))
                } else {
                    navController.navigate(route = MovieInfoRoute(media[page].id))
                }
            },
            medias = media[page],
            onPlay = { },
            info = media[page].plot.toString())
    }
}


@Composable
private fun Herocard(
    onClick: () -> Unit,
    medias: MediaCardUiModel,
    onPlay: () -> Unit,
    info: String
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val transition = updateTransition(expanded, label = "")

    val widthFraction by transition.animateFloat(
        transitionSpec = { tween(600) },
        label = ""
    ) {
        if (it) 0.9f else 0.18f
    }

    LaunchedEffect(expanded) {
        if(!expanded){
            delay(2000)
            expanded=true
        }else {
            delay(8000)
            expanded=false
        }
    }

    Card(
    Modifier
        .height(250.dp)
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 5.dp)
        .background(shape = RoundedCornerShape(20.dp), color = VMocha.Crust)
        .clickable(onClick = onClick)
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackdrop(
            image = medias.backdrop,
            duraction = 1500,
            modifier = Modifier.clip(shape = RoundedCornerShape(20.dp))
        )


        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .height(65.dp)
                .fillMaxWidth(widthFraction)
                .background(
                    VMocha.Mantle, RoundedCornerShape(0, 100, 100, 20)
                )
        )
        {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 5.dp, vertical = 10.dp)
                    .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Column(Modifier.fillMaxHeight()) {

                        TypewriterText(
                            medias.title,  // title ,
                            color = VMocha.Text,
                            maxLines = 1,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            // modifier = Modifier.fillMaxWidth()
                        )
                    }
                }



                IconButton(
                    modifier = Modifier.size(65.dp, 65.dp),
                    onClick =onPlay
                ) {

                    Icon(
                        Icons.Default.PlayCircle,
                        null,
                        tint = VMocha.Text,
                        modifier = Modifier
                            .size(60.dp)
                    )
                }
            }

        }


    }

}
    
}

