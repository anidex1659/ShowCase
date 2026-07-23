
package com.example.showcase.features.MetaData.data.scanner




import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.example.showcase.core.progresmanager.ProgressManager
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.model.MediaFile
import com.example.showcase.features.MetaData.data.scanner.Scanners.LibraryScanner
import com.example.showcase.features.MetaData.data.scanner.Scanners.MovieScanner
import com.example.showcase.features.MetaData.data.scanner.Scanners.MusicScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScannerManager(

    private val dao: ShowcaseDao,

    private val libraryScanner: LibraryScanner,

    private val movieScanner: MovieScanner,

    private val musicScanner: MusicScanner
) {
    private suspend fun scanLibrary(
        context: Context,
        library: LibraryEntity
    )  = withContext(Dispatchers.IO){

        ProgressManager.update(

            title = "Scanning ${library.name} ",

            subtitle = library.type,

            current = + 1,

            total = 0
        )


        cleanupMissingMedia(
            context = context,
            library = library
        )

        Log.d(
            "Anidex_ScannerManager",
            "Scanning Library : ${library.name} ${library.type}"
        )
        val root =
            DocumentFile.fromTreeUri(
                context,
                Uri.parse(
                    library.folderUri
                )
            ) ?: return@withContext

        val mediaFiles =
            mutableListOf<MediaFile>()

        collectMediaFiles(
            root = root,
            mediaFiles = mediaFiles
        )

        when (library.type) {

            "Series" ->
                libraryScanner.scan(
                    library.id,
                    mediaFiles
                )

            "Movies" ->
                movieScanner.scan(
                    library.id,
                    mediaFiles
                )

            "Music" ->
                musicScanner.scan(
                    library.id,
                    mediaFiles
                )
        }
    }



     suspend fun cleanupMissingMedia(
        context: Context,
        library: LibraryEntity
    ) {
        when (library.type) {

            "Series" -> {

                val seriesList =
                    dao.getSeriesByLibraryId(library.id)

                seriesList.forEach { series ->

                    val folder =
                        DocumentFile.fromTreeUri(
                            context,
                            Uri.parse(series.folderPath)
                        )

                    if (folder == null || !folder.exists()) {

                        Log.d(
                            "Anidex_Cleanup",
                            "Removing missing series: ${series.title}"
                        )

                        dao.deleteSeriesById(series.id)
                    }
                }
            }

            "Movies" -> {

                val movies =
                    dao.getMoviesByLibraryId(library.id)

                movies.forEach { movie ->

                    val file =
                        DocumentFile.fromSingleUri(
                            context,
                            Uri.parse(movie.filePath)
                        )

                    if (file == null || !file.exists()) {

                        Log.d(
                            "Anidex_Cleanup",
                            "Removing missing movie: ${movie.title}"
                        )

                        dao.deleteMovieById(movie.id)
                    }
                }
            }
        }
    }


    suspend fun scanLibraryByName(
        context: Context,
        name: String
    ) {
        Log.d(
            "Anidex_ScannerManager",
            "Scanning Library By Name : $name "
        )

        val library = dao.getLibraryByName(name) ?: return
        scanLibrary(
            context = context, library = library
        )
    }

    suspend fun scanLibraryByType(
        context: Context,
        type: String
    ) {
        Log.d(
            "Anidex_ScannerManager",
            "Scanning Library By Type : $type "
        )

        val libraries = dao.getLibrariesByType(type)

        libraries.forEach {
            scanLibrary(
                context = context, library = it
            )
        }

    }

    suspend fun scanAllLibraries(
        context: Context
    ) = withContext(Dispatchers.IO) {
        Log.d(
            "Anidex_ScannerManager",
            "Scanning All Libraries "
        )
        val libraries = dao.getLibraries()

        libraries.forEach {

            scanLibrary(
                context = context, library = it
            )
        }
    }

    private fun collectMediaFiles(
        root: DocumentFile,
        mediaFiles: MutableList<MediaFile>
    ) {

        root.listFiles().forEach {
            when {
                it.isDirectory -> {
                    collectMediaFiles(
                        root = it, mediaFiles = mediaFiles
                    )
                }
                it.isFile -> {
                    val name = it.name ?: return@forEach
                    if (
                        name.endsWith(
                            ".mkv", true
                        )
                        ||
                        name.endsWith(
                            ".mp4", true
                        )
                        ||
                        name.endsWith(
                            ".avi", true
                        )
                        ||
                        name.endsWith(
                            ".mov", true
                        )
                    ) {

                        mediaFiles.add(
                            MediaFile(
                                name = name,
                                uri = it.uri,
                                parentFolder = root.uri.toString(),
                                parentFolderName = root.name
                            )
                        )
                    }
                }
            }
        }
    }
}



//                              LibraryEntity
//                                    │
//                                    ▼
//                              ScannerManager
//                                    │
//                                    ├── Cleanup old DB entries
//                                    │
//                                    ├── Open library folder using SAF
//                                    │
//                                    ├── Recursively find media files
//                                    │
//                                    ▼
//                                MediaFile[]
//                                    │
//                    ├───────────────┬────────────────┐
//                    ▼               ▼                ▼
//              Series Scanner   Movie Scanner   Music Scanner
//                    │               │
//                    ▼               ▼
//              SeriesDetector   MovieDetector
//                    │               │
//                    ▼               ▼
//              SeriesEntity     MovieEntity
//                    │               │
//                    ▼               ▼
//              TMDB Metadata    TMDB Metadata
//                    │               │
//                    ▼               ▼
//              JSON Metadata    JSON Metadata
//                    │               │
//                    ▼               ▼
//              Artwork Download Artwork Download
//                    │               │
//                    ▼               ▼
//              EpisodeEntity    Database Complete
