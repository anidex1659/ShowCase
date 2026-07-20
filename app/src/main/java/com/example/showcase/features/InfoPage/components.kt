package com.example.showcase.features.InfoPage

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.showcase.R
import com.example.showcase.features.MetaData.data.model.MovieUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.ui.theme.VMocha

@Preview(showBackground = true)
@Composable
fun BackDroupCard() {
    val scale by rememberInfiniteTransition(label = "").animateFloat(
            initialValue = 1f, targetValue = 1.08f, animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200, easing = FastOutSlowInEasing
                ), repeatMode = RepeatMode.Reverse
            ), label = ""
        )
    Column() {

        Card(
            shape = RoundedCornerShape(20, 20, 20, 20), colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            //border = BorderStroke(width = 5.dp, color = VMocha.Blue),
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .height(220.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    shape = RoundedCornerShape(20, 20, 20, 20),
                    border = BorderStroke(width = 5.dp, color = VMocha.Blue),
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 30.dp)
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(R.drawable.folder),//backdroup image tobe changed
                        contentDescription = "home_screen_BackGround",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .height(40.dp)
                        .width(80.dp)
                        .background(
                            VMocha.Base, RoundedCornerShape(0, 50, 0, 50)
                        )
                ) {
                    Text(
                        "7.8", // RATING
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = VMocha.Teal,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 5.dp),
                        textAlign = TextAlign.Center

                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(80.dp)
                        .width(80.dp)
                        .background(
                            VMocha.Base, RoundedCornerShape(100)
                        )
                ) {
                    IconButton(
                        modifier = Modifier.fillMaxSize(), onClick = {}) {
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
//@Preview(showBackground = true)
@Composable
fun PosterCard2(series: SeriesUiModel, onClick: () -> Unit, onPlayClick: () -> Unit) {
    val scale by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 1f, targetValue = 1.08f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200, easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )
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
                .width(150.dp).clickable(onClick=onClick)
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
                        model = series.poster?.uri,//backdroup image tobe changed
                        contentDescription = "thumb",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )
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
                        text = series.series.title, // title
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
                        modifier = Modifier.fillMaxSize(), onClick = onPlayClick
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



@Composable
fun MoviePosterCard2(movies: MovieUiModel, onClick: () -> Unit, onPlayClick: () -> Unit) {
    val scale by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 1f, targetValue = 1.08f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200, easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val poster =
        movies.artwork.firstOrNull {
            it.type == "poster"
        }

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
                .width(150.dp).clickable(onClick=onClick)
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
                        model = poster?.uri,//backdroup image tobe changed
                        contentDescription = "thumb",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )
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
                        text = movies.movie.title, // title
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
                        modifier = Modifier.fillMaxSize(), onClick = onPlayClick
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
