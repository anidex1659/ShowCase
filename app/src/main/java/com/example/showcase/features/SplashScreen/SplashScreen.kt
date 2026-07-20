package com.example.showcase.features.SplashScreen

import android.window.SplashScreen
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.media3.extractor.TrueHdSampleRechunker
import androidx.navigation.NavHostController
import androidx.xr.scenecore.runtime.Resource
import com.example.showcase.R
import com.example.showcase.features.Library.ui.Components.TypewriterText
import com.example.showcase.features.SplashScreen.model.splashItems
import com.example.showcase.ui.navigation.HomeRoute
import com.example.showcase.ui.navigation.LibraryRoute
import com.example.showcase.ui.theme.VMocha
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SplashScreen(
    navController: NavHostController
) {

    val isReady = MutableStateFlow(false)

    LaunchedEffect(Unit) {

        delay(2500)
        navController.navigate(HomeRoute)
    }

    SplashScreenImg()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenImg() {


    var leftItem by remember {
        mutableStateOf(splashItems.random())
    }

    var rightItem by remember {
        mutableStateOf(
            splashItems
                .filter { it != leftItem }
                .random()
        )
    }

    var backgroundColor by remember {
        mutableStateOf(
            listOf(
                VMocha.Blue,
                VMocha.Lavender,
                VMocha.Green,
                VMocha.Mauve,
                VMocha.Sapphire
            ).random()
        )
    }

    var start by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        start = true
    }

    val leftWidth by animateDpAsState(
        targetValue = if (start) 175.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val rightWidth by animateDpAsState(
        targetValue = if (start) 175.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val leftHight by animateDpAsState(
        targetValue = if (start) 350.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val rightHight by animateDpAsState(
        targetValue = if (start) 350.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val cardHeight by animateDpAsState(
        targetValue = if(start) 700.dp else 0.dp,
        animationSpec = tween(1000)
    )


    Box(
        Modifier
            .fillMaxSize()
            .background(VMocha.Crust)
            .padding(20.dp),
    ){


        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(cardHeight)
                .background(
                    backgroundColor,
                    shape = RoundedCornerShape(20.dp)
                ).clickable{

                    leftItem = splashItems.random()

                    rightItem = splashItems
                        .filter { it != leftItem }
                        .random()

                    backgroundColor =
                        listOf(
                            VMocha.Blue,
                            VMocha.Lavender,
                            VMocha.Green,
                            VMocha.Mauve,
                            VMocha.Sapphire
                        ).random()
                }
            //Auto chasing color Backcard
        )


        Box(Modifier.fillMaxWidth()
            .padding(top = 20.dp, end = leftWidth)){ //top title
            Text(
                leftItem.title,
                color = VMocha.Text,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        Box(Modifier
            .align(Alignment.TopEnd) //Top card
            .size(width = leftWidth, leftHight)
            .background(
                VMocha.Crust,
                shape = RoundedCornerShape(bottomStart = 20.dp)
            )
        )
        {

            Image(
                painter = painterResource(id =leftItem.image), "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxSize(0.9f)
                    .clip(RoundedCornerShape(20.dp))

            )

            Box(Modifier.align(Alignment.CenterEnd)
                .fillMaxSize(0.9f)
                .background(
                    VMocha.Lavender.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp))
            )

        }



        Box(Modifier.fillMaxWidth()
            .align(Alignment.BottomStart)
            .padding(bottom = 20.dp, start = rightWidth)){//bottom title
            Text(
                rightItem.title,
                color = VMocha.Text,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }

        Box(Modifier
            .align(Alignment.BottomStart) //Bottom Card
            .size(width = rightWidth, rightHight)
            .background(
                VMocha.Crust,
                shape = RoundedCornerShape(topEnd = 20.dp)
            )
        )
        {

            Image(
                painter = painterResource(id = rightItem.image), "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxSize(0.9f)
                    .clip(RoundedCornerShape(20.dp))

            )

            Box(Modifier.align(Alignment.CenterStart)
                .fillMaxSize(0.9f)
                .background(
                    VMocha.Lavender.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp))
            )

        }




        TypewriterText(
            "ShowCase",
            typingSpeed = 5,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif),
            color = VMocha.Text,
            modifier = Modifier.align(Alignment.Center))


    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoadingPopup(
    visible: Boolean = false,
    text: String = "Scanning Library..."
){
    if (!visible) return

    var leftItem by remember {
        mutableStateOf(splashItems.random())
    }

    var rightItem by remember {
        mutableStateOf(
            splashItems
                .filter { it != leftItem }
                .random()
        )
    }

    var backgroundColor by remember {
        mutableStateOf(
            listOf(
                VMocha.Blue,
                VMocha.Lavender,
                VMocha.Green,
                VMocha.Mauve,
                VMocha.Sapphire
            ).random()
        )
    }

    var start by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        start = true
    }

    val leftWidth by animateDpAsState(
        targetValue = if (start) 135.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val rightWidth by animateDpAsState(
        targetValue = if (start) 135.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val leftHight by animateDpAsState(
        targetValue = if (start) 215.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val rightHight by animateDpAsState(
        targetValue = if (start) 215.dp else 0.dp,
        animationSpec = tween(1200),
        label = ""
    )

    val cardHeight by animateDpAsState(
        targetValue = if(start) 400.dp else 0.dp,
        animationSpec = tween(1000)
    )


    Dialog(onDismissRequest = {})
    {


        Box(
            Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(VMocha.Crust, shape = RoundedCornerShape(20.dp))
                .padding(20.dp),
        ){


            Box(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(
                        backgroundColor,
                        shape = RoundedCornerShape(20.dp)
                    ).clickable{

                        leftItem = splashItems.random()

                        rightItem = splashItems
                            .filter { it != leftItem }
                            .random()

                        backgroundColor =
                            listOf(
                                VMocha.Blue,
                                VMocha.Lavender,
                                VMocha.Green,
                                VMocha.Mauve,
                                VMocha.Sapphire
                            ).random()
                    }
                //Auto chasing color Backcard
            )


            Box(Modifier.fillMaxWidth()
                .padding(top = 5.dp, end = leftWidth)){  //top title
                Text(
                    leftItem.title,
                    color = VMocha.Text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            Box(Modifier
                .align(Alignment.TopEnd) //Top card
                .size(width = leftWidth, leftHight)
                .background(
                    VMocha.Crust,
                    shape = RoundedCornerShape(bottomStart = 20.dp)
                )
            )
            {

                Image(
                    painter = painterResource(id =leftItem.image), "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(20.dp))

                )

                Box(Modifier.align(Alignment.CenterEnd)
                    .fillMaxSize(0.9f)
                    .background(
                        VMocha.Lavender.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp))
                )

            }



            Box(Modifier.fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(bottom = 5.dp, start = rightWidth)){//bottom title
                Text(
                    rightItem.title,
                    color = VMocha.Text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }

            Box(Modifier
                .align(Alignment.BottomStart) //Bottom Card
                .size(width = rightWidth, rightHight)
                .background(
                    VMocha.Crust,
                    shape = RoundedCornerShape(topEnd = 20.dp)
                )
            )
            {

                Image(
                    painter = painterResource(id = rightItem.image), "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(20.dp))

                )

                Box(Modifier.align(Alignment.CenterStart)
                    .fillMaxSize(0.9f)
                    .background(
                        VMocha.Lavender.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp))
                )

            }




            TypewriterText(
                text,
                typingSpeed = 5,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif),
                color = VMocha.Text,
                modifier = Modifier.align(Alignment.Center))


        }

    }
}