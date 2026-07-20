package com.example.showcase.features.Library.repository

import android.content.Context
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.model.LibraryUiModel
import com.example.showcase.features.MetaData.data.repository.metadata.movie.MovieRepository
import com.example.showcase.features.MetaData.data.repository.metadata.sires.SeriesRepository
import com.example.showcase.features.MetaData.data.scanner.ScannerManager

class LibraryRepository(

    private val dao: ShowcaseDao,

    private val scannerManager: ScannerManager,

    private val seriesRepository: SeriesRepository,

    private val movieRepository: MovieRepository

) {

    suspend fun getLibraries(): List<LibraryUiModel> {

        return dao.getLibraries().map { library ->

            when (library.type.uppercase()) {

                "SERIES" -> {

                    val series =
                        seriesRepository.getSeriesByLibrary(
                            library.id
                        )

                    LibraryUiModel(

                        library = library,

                        series = series,

                        totalItems = series.size
                    )
                }

                "MOVIES" -> {

                    val movies =
                        movieRepository.getMovies(
                            library.id
                        )

                    LibraryUiModel(

                        library = library,

                        movies = movies,

                        totalItems = movies.size
                    )
                }

                else -> {

                    LibraryUiModel(

                        library = library,

                        totalItems = 0

                    )
                }
            }
        }
    }

    suspend fun addLibrary(
        library: LibraryEntity
    ) {

        dao.insertLibrary(library)
    }

    suspend fun deleteLibrary(
        id: Long
    ) {

        dao.deleteLibrary(id)
    }

    suspend fun scanAllLibraries(
        context: Context
    ) {

        scannerManager.scanAllLibraries(context)
    }

    suspend fun scanLibraryByName(

        context: Context,

        name: String

    ) {

        scannerManager.scanLibraryByName(

            context,

            name

        )
    }
}