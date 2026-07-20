package com.example.showcase.features.InfoPage

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.core.progresmanager.ScanProgress
import com.example.showcase.features.HomeScreen.BootomBAR
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
import com.example.showcase.features.Player.PlayerViewModel.PlayerViewModel
import com.example.showcase.features.Player.repository.PlayerRepository
import com.example.showcase.features.SplashScreen.SplashScreenImg
import com.example.showcase.ui.navigation.PlayerGraph
import com.example.showcase.ui.navigation.PlayerRoute
import com.example.showcase.ui.navigation.PlayerType
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InfoPage(movieId: Long? = null, seriesId: Long? = null, navController: NavHostController) {


    val context =
        LocalContext.current

    val progress by ProgressManager
        .progress
        .collectAsState()

    val dao = DatabaseProvider
        .getDatabase(context)
        .dao()




    val jsonmanger= MetadataJsonManager(dao)
    val movieMetadataJsonManager = MovieMetadataJsonManager(dao)

    val MetadataRepository = MetadataStorageRepository(context,jsonmanger,movieMetadataJsonManager)

    val repository = remember {
        InfoRepository(
            dao = dao,
            seriesRepository = SeriesRepository(dao),
            movieRepository = MovieRepository(dao),
            MetadataRepository
        )
    }

    PlayerGraph.initialize(
        context = context,
        repository = PlayerRepository(
            SeriesRepository(dao),
            MovieRepository(dao),
            dao,
            MetadataRepository
        )
    )
    val playerViewModel = remember {

        PlayerViewModel(
            PlayerGraph.playerManager,
            PlayerGraph.repository
        )
    }

    val viewModel: InfoViewModel = viewModel(
        factory = InfoViewModelFactory(repository)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId, seriesId) {

        when {

            movieId != null ->
                viewModel.loadMovie(movieId)

            seriesId != null ->
                viewModel.loadSeries(seriesId)
        }
    }

    when {

        uiState.loading -> {
            SplashScreenImg()
        }
        uiState.movie != null -> {
            Column (Modifier.fillMaxSize()) {
                MovieInfoContent(
                    movie = uiState.movie!!,
                    playerViewModel,
                    navController,
                    progress
                )
                BootomBAR(navController, progress)
            }
        }

        uiState.series != null -> {
            Column (Modifier.fillMaxSize()) {
                SeriesInfoContent(
                    series = uiState.series!!,
                    playerViewModel,
                    navController,
                    progress

                )
                BootomBAR(navController, progress)
            }
        }
    }


}

//@Preview
@Composable
fun HeroCard(
    rating: String,

    poster: Any?,

    logo: Any?,
    onplay: () -> Unit
) {
    var badgeVisible by remember {
        mutableStateOf(false)
    }

    var playVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        badgeVisible = true

        delay(600)

        playVisible = true
    }

    val playScale by animateFloatAsState(

        targetValue =
            if (playVisible) 1f else 0.5f,

        animationSpec = spring(

            dampingRatio =
                Spring.DampingRatioMediumBouncy,

            stiffness =
                Spring.StiffnessLow
        ),

        label = ""
    )

    val scale by rememberInfiniteTransition(label = "")
        .animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )
    Column() {

        Card(
            shape = RoundedCornerShape(10, 10, 10, 0),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),

            //border = BorderStroke(width = 5.dp, color = VMocha.Blue),
            modifier = Modifier
                .padding(20.dp, 10.dp, 20.dp, 0.dp)
                .height(500.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    shape = RoundedCornerShape(10, 20, 10, 20),
                    border = BorderStroke(width = 1.dp, color = VMocha.Lavender),
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 30.dp)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = poster,//poster image tobe changed
                        contentDescription = "home_screen_BackGround",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )
                }


                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .height(40.dp)
                            .width(80.dp)
                            .background(
                                VMocha.Mantle,
                                RoundedCornerShape(0, 50, 0, 50)
                            )
                    ) {

                        Text(
                            rating,
                            color = VMocha.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 5.dp)
                        )
                    }


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(120.dp)
                        .background(
                            VMocha.Mantle,
                            RoundedCornerShape(100)
                        )
                )
                {
                    IconButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = onplay
                    ) {

                        Icon(
                            Icons.Default.PlayCircle,
                            null,
                            tint = VMocha.Text,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .graphicsLayer(
                                    scaleX = playScale * scale,
                                    scaleY = playScale * scale
                                )
                        )
                    }
                }

            }


        }

        AsyncImage(
            model = logo,
            contentDescription = "Series Logo",
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 10.dp),
            contentScale = ContentScale.Fit
        )

    }


}


//@Preview
@Composable
private fun InfoPanel(  genres: List<String>, plot: String, year: Int, status: String)
{
    LazyColumn(
        modifier = Modifier
            .height(250.dp)
            //.fillMaxHeight()
            .background(VMocha.Mantle, RoundedCornerShape(10, 10, 10, 10))
            .border(1.dp, VMocha.Lavender, RoundedCornerShape(10, 10, 10, 10))
    ) {
        item {
            TypewriterText(
                genres.joinToString(" • "), //PLUNGE IN GENRES //Add typing animestion
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VMocha.Text,
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))

        }
        item {
            Text(
               plot,
                color = VMocha.Subtext1,
                lineHeight = 24.sp,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
        item {
            Text(
                "<$year"+"|"+"$status>",
                color = VMocha.Subtext0,
                textAlign = TextAlign.End,
                lineHeight = 24.sp,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        }
    }
}




@Composable
fun SeriesInfoContent(

    series: SeriesInfoUiModel,
    playerViewModel: PlayerViewModel,
    navController: NavHostController,
    progress: ScanProgress
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .background(VMocha.Crust)
            .fillMaxSize()) {

            Spacer(modifier = Modifier.padding(25.dp))
            HeroCard(

                rating =
                    series.metadata?.rating?.toString()
                        ?: "--",

                poster = series.media.poster?.uri,

                logo = series.media.logo?.uri,
                onplay = {
                    navController.navigate(PlayerRoute(series.media.series.id, type = PlayerType.SERIES))}
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Box(){
            InfoPanel(

                genres =
                    series.metadata?.genres
                        ?: emptyList(),

                plot =
                    series.metadata?.plot
                        ?: "",

                year =
                    series.metadata?.year
                        ?: 0,

                status =
                    series.metadata?.status
                        ?: ""
            )

            }
        }
    }
}

@Composable
fun MovieInfoContent(

    movie: MovieInfoUiModel,
    playerViewModel: PlayerViewModel,
    navController: NavHostController,
    progress: ScanProgress
) {

    Column (modifier = Modifier
        .background(VMocha.Crust)
        .fillMaxSize()

    ){

        TypewriterText(

            movie.metadata?.tagline
                ?: "",
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,

            color = VMocha.Text
        )

        HeroCard(

            rating =
                movie.metadata?.rating?.toString()
                    ?: "--",

            poster =
                movie.movie.artwork.firstOrNull {
                    it.type == "poster"
                }?.uri,

            logo =
                movie.movie.artwork.firstOrNull {
                    it.type == "logo"
                }?.uri,

            {

                navController.navigate(PlayerRoute(movie.movie.movie.id, type = PlayerType.MOVIE))}
        )

        Box() {
            InfoPanel(

                genres =
                    movie.metadata?.genres
                        ?: emptyList(),

                plot =
                    movie.metadata?.plot
                        ?: "",

                year =
                    movie.metadata?.year
                        ?: 0,

                status =
                    movie.metadata?.status
                        ?: ""
            )
        }

    }
}