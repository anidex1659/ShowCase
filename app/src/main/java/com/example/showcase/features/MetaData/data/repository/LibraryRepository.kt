package com.example.showcase.features.MetaData.data.repository

import android.content.Context
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.model.EpisodeUiModel
import com.example.showcase.features.MetaData.data.model.LibraryUiModel
import com.example.showcase.features.MetaData.data.model.SeasonUiModel
import com.example.showcase.features.MetaData.data.model.SeriesUiModel
import com.example.showcase.features.MetaData.data.scanner.ScannerManager


//class LibraryRepository(
//
//    private val dao: ShowcaseDao,
//    private val scannerManager: ScannerManager
//) {
//
//    suspend fun getLibraries():
//            List<LibraryUiModel> {
//
//        return dao
//            .getLibraries()
//            .map { library ->
//
//                val series =
//                    dao.getSeriesForLibrary(
//                        library.id
//                    )
//
//                LibraryUiModel(
//
//                    library = library,
//
//                    series =
//                        series.map { seriesEntity ->
//
//                            val episodes =
//                                dao.getEpisodesForSeries(
//                                    seriesEntity.id
//                                )
//
//                            SeriesUiModel(
//
//                                series = seriesEntity,
//
//                                seasons =
//                                    episodes
//                                        .groupBy {
//                                            it.seasonNumber
//                                        }
//                                        .map { season ->
//
//                                            SeasonUiModel(
//
//                                                seasonNumber =
//                                                    season.key,
//
//                                                episodes =
//                                                    season.value.map {
//
//                                                        EpisodeUiModel(
//                                                            episode = it
//                                                        )
//                                                    }
//                                            )
//                                        }
//                            )
//                        }
//                )
//            }
//    }
//
//    suspend fun addLibrary(
//        library: LibraryEntity
//    ) {
//
//        dao.insertLibrary(
//            library
//        )
//    }
//
//    suspend fun deleteLibrary(
//        id: Long
//    ) {
//
//        dao.deleteLibrary(id)
//    }
//
//    suspend fun scanAllLibraries(
//        context: Context
//    ) {
//
//        scannerManager
//            .scanAllLibraries(
//                context
//            )
//    }
//
//    suspend fun scanLibraryByName(
//        context: Context,
//        name: String
//    ) {
//
//        scannerManager
//            .scanLibraryByName(
//                context,
//                name
//            )
//    }
//
//    suspend fun scanLibraryByType(
//        context: Context,
//        type: String
//    ) {
//
//        scannerManager
//            .scanLibraryByType(
//                context,
//                type
//            )
//    }
//}