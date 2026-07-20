package com.example.showcase.features.Library.ui.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showcase.core.progresmanager.ScanProgress
import com.example.showcase.features.MetaData.data.model.LibraryUiModel
import com.example.showcase.features.MetaData.data.model.SeasonUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.ui.popup.DialogManager
import com.example.showcase.ui.popup.GlobalDialog
import com.example.showcase.ui.theme.VMocha
import com.example.showcase.features.MetaData.data.model.MovieUiModel
import kotlinx.coroutines.delay
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryPanel(
    libraries: List<LibraryUiModel>,
    onScanLibrary: (LibraryUiModel) -> Unit,
    progress: ScanProgress
) {



    if (libraries.isNotEmpty())
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth().padding(5.dp)
                .background(
                    VMocha.Base, RoundedCornerShape(20.dp)
                )
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(
                libraries.size
            ) { index ->

                val library =
                    libraries[index]

                LibraryCard(
                    library = library,
                    onScan = {
                        onScanLibrary(library)
                    }
                )
            }
        }
    else if (progress.visible)
        TypewriterText(
            if (!progress.visible)
                ""
            else
                "${progress.title} | <${progress.subtitle}>",
            textAlign = TextAlign.Center,
            color = VMocha.Subtext1,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier.fillMaxWidth()
                .padding(top=250.dp, bottom = 20.dp))
    else
       TypewriterText("No Library Found | Add Library",
           textAlign = TextAlign.Center,
           color = VMocha.Subtext1,
           fontWeight = FontWeight.Bold ,
           modifier = Modifier.fillMaxWidth()
               .padding(top=250.dp, bottom = 20.dp))
}

@Composable

fun LibraryCard(
    library: LibraryUiModel,
    onScan: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = VMocha.Mantle
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                //horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.9f),
                text = library.library.name,

                color = VMocha.Text,

                fontSize = 25.sp,

                fontWeight = FontWeight.Bold
            )
                IconButton(
                    modifier = Modifier.size(28.dp),
                    onClick = onScan) {

                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = VMocha.Text
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            when (
                library.library.type.uppercase()
            ) {

                "SERIES" -> {

                    library.series.forEach {

                        SeriesCard(
                            series = it
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )
                    }
                }

                "MOVIES" -> {

                    library.movies.forEach { movie ->

                        MovieCard(
                            movie = movie
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SeriesCard(
    series: SeriesUiModel
) {

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = VMocha.Base
        )
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(10.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        if (expanded)
                            Icons.Default.KeyboardArrowDown
                        else
                            Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = VMocha.Text
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = series.series.title,
                    color = VMocha.Text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        DialogManager.show(
                            GlobalDialog.IdentifySeries(
                                series.series.id
                            )
                        )
                    }
                ) {
                    Icon(
                        Icons.Default.Search,
                        null,
                        tint = VMocha.Text
                    )
                }
            }

            AnimatedVisibility(expanded) {

                Column(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    )
                ) {

                    series.seasons.forEach {

                        SeasonCard(it)

                        Spacer(
                            Modifier.height(6.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SeasonCard(
    season: SeasonUiModel
) {

    Column {

        Text(
            text = "Season ${season.seasonNumber}",

            color = VMocha.Blue,

            fontSize = 18.sp,

            fontWeight = FontWeight.Bold
        )

        season.episodes.forEach {

            EpisodeRow(
                episodeTitle =
                    it.episode.title
            )
        }
    }
}

@Composable
fun EpisodeRow(
    episodeTitle: String
) {

    Row(
        modifier = Modifier.padding(
            start = 20.dp, top = 4.dp
        )
    ) {

        Text(
            "├──", color = VMocha.Subtext0
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = episodeTitle,
            color = VMocha.Text
        )
    }
}
@Composable
fun MovieCard(
    movie: MovieUiModel
) {

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = VMocha.Base
        )
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(10.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        if (expanded)
                            Icons.Default.KeyboardArrowDown
                        else
                            Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = VMocha.Text
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = movie.movie.title,
                    color = VMocha.Text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        // Identify Movie
                    }
                ) {

                    Icon(
                        Icons.Default.Search,
                        null,
                        tint = VMocha.Text
                    )
                }
            }

            AnimatedVisibility(expanded) {

                Column(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    )
                ) {

                    ArtworkRow(
                        "Poster",
                        movie.artwork.any {
                            it.type == "poster"
                        }
                    )

                    ArtworkRow(
                        "Backdrop",
                        movie.artwork.any {
                            it.type == "backdrop"
                        }
                    )

                    ArtworkRow(
                        "Logo",
                        movie.artwork.any {
                            it.type == "logo"
                        }
                    )

                    movie.playback?.let {

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            "Progress: ${it.position}/${it.duration}",
                            color = VMocha.Subtext0
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ArtworkRow(

    artworkType: String,

    available: Boolean

) {

    Row(
        modifier = Modifier.padding(
            start = 20.dp,
            top = 4.dp
        )
    ) {

        Text(
            "├──",
            color = VMocha.Subtext0
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(

            text = artworkType,

            color =
                if (available)
                    VMocha.Green
                else
                    VMocha.Red

        )
    }
}


@Composable
fun TypewriterText(

    text: String,

    modifier: Modifier = Modifier,

    color: Color = Color.Unspecified,

    fontSize: TextUnit = TextUnit.Unspecified,

    fontWeight: FontWeight? = null,

    textAlign: TextAlign? = null,

    style: TextStyle = MaterialTheme.typography.bodyLarge,

    maxLines: Int = Int.MAX_VALUE,

    overflow: TextOverflow = TextOverflow.Clip,

    typingSpeed: Long = 40L,

    showTypingCursor: Boolean = true

) {

    var visibleText by remember {
        mutableStateOf("")
    }

    var cursorVisible by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(text) {

        visibleText = ""

        text.forEach {

            visibleText += it

            delay(typingSpeed)
        }
    }

    if (showTypingCursor) {

        LaunchedEffect(Unit) {

            while (true) {

                cursorVisible = !cursorVisible

                delay(500)
            }
        }
    }

    Text(

        modifier = modifier,

        text = visibleText + if (showTypingCursor && cursorVisible) "|" else "",

        color = color,

        fontSize = fontSize,

        fontWeight = fontWeight,

        textAlign = textAlign,

        style = style,

        maxLines = maxLines,

        overflow = overflow
    )
}

