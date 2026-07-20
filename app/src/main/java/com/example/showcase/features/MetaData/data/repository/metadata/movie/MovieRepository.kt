package com.example.showcase.features.MetaData.data.repository.metadata.movie
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieEntity
import com.example.showcase.features.MetaData.data.model.MovieUiModel


class MovieRepository(

     val dao: ShowcaseDao

) {

    suspend fun getMovies(
        libraryId: Long
    ): List<MovieUiModel> {

        return dao
            .getMovies(libraryId)
            .map {
                buildMovieUiModel(it)
            }
    }

    suspend fun getMovieById(
        id: Long
    ): MovieUiModel? {

        val movie =
            dao.getMovieById(id)
                ?: return null

        return buildMovieUiModel(movie)
    }

    suspend fun getMovieByTitle(
        title: String
    ): MovieUiModel? {

        val movie =
            dao.getMovieByTitle(title)
                ?: return null

        return buildMovieUiModel(movie)
    }

    suspend fun getRecentlyAdded(
        limit: Int = 20
    ): List<MovieUiModel> {

        return dao
            .getRecentlyAddedMovies(limit)
            .map {
                buildMovieUiModel(it)
            }
    }

    private suspend fun buildMovieUiModel(
        movie: MovieEntity
    ): MovieUiModel {

        val artwork =
            dao.getMovieArtwork(
                movie.id
            )

        val playback =
            dao.getMoviePlayback(
                movie.id
            )

        return MovieUiModel(

            movie = movie,

            artwork = artwork,

            playback = playback

        )
    }
}