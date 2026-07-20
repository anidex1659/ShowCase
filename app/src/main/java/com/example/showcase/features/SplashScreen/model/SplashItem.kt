package com.example.showcase.features.SplashScreen.model


import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.showcase.R
import com.example.showcase.ui.theme.VMocha

data class SplashItem(

    val title: String,

    @DrawableRes
    val image: Int,
    val color: Color = VMocha.Lavender
)

val splashItems = listOf(

    SplashItem(
        title = "Anime",
        image = R.drawable.anime
    ),

    SplashItem(
        title = "C-Drama",
        image = R.drawable.dramac
    ),

    SplashItem(
        title = "K-Drama",
        image = R.drawable.kdrama
    ),

    SplashItem(
        title = "BollyWood",
        image = R.drawable.bollywood
    ),

    SplashItem(
        title = "Sci-Fi",
        image = R.drawable.sifi
    ),

    SplashItem(
        title = "Comedy",
        image = R.drawable.comady
    ),

    SplashItem(
        title = "Action",
        image = R.drawable.action
    ),

    SplashItem(
        title = "Comedy",
        image = R.drawable.comady
    ),

    SplashItem(
        title = "Anime",
        image = R.drawable.anime2
    ),

    SplashItem(
        title = "Cartoon",
        image = R.drawable.catoon
    )


)