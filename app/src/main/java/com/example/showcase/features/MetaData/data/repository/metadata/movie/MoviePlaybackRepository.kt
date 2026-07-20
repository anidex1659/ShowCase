package com.example.showcase.features.MetaData.data.repository.metadata.movie

import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity

class MoviePlaybackRepository(

    private val dao: ShowcaseDao

) {

    suspend fun getContinueWatching():
            List<MoviePlaybackEntity> {

        return dao.getMovieContinueWatching()
    }

    suspend fun getPlaybackHistory():
            List<MoviePlaybackEntity> {

        return dao.getMoviePlaybackHistory()
    }

    suspend fun savePlayback(

        movieId: Long,

        position: Long,

        duration: Long

    ) {

        dao.insertMoviePlayback(

            MoviePlaybackEntity(

                movieId = movieId,

                position = position,

                duration = duration,

                lastPlayed = System.currentTimeMillis()

            )

        )
    }
}