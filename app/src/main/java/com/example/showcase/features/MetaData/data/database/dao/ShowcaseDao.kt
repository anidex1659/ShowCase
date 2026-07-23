package com.example.showcase.features.MetaData.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.showcase.features.MetaData.data.database.relation.SeriesComplete
import com.example.showcase.features.MetaData.data.database.entity.series.ArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.series.EpisodeEntity
import com.example.showcase.features.MetaData.data.database.entity.LibraryEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MovieEntity
import com.example.showcase.features.MetaData.data.database.entity.movie.MoviePlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicArtworkEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicEntity
import com.example.showcase.features.MetaData.data.database.entity.music.MusicPlaybackEntity
import com.example.showcase.features.MetaData.data.database.entity.series.PlaybackHistoryEntity
import com.example.showcase.features.MetaData.data.database.entity.series.SeriesEntity
import com.example.showcase.features.MetaData.data.database.relation.LibraryComplete

@Dao
interface ShowcaseDao {

    @Insert
    suspend fun insertLibrary(
        library: LibraryEntity
    ): Long

    @Transaction
    @Query(
        """
    SELECT * FROM libraries
    WHERE id = :libraryId
    """
    )
    suspend fun getLibraryComplete(
        libraryId: Long
    ): LibraryComplete?

    @Query(
        "SELECT * FROM libraries"
    )
    suspend fun getLibraries():
            List<LibraryEntity>

    @Query(
        "SELECT * FROM libraries WHERE id = :id"
    )
    suspend fun getLibrary(
        id: Long
    ): LibraryEntity?

    @Query(
        "SELECT * FROM libraries WHERE type = :type"
    )
    suspend fun getLibrariesByType(
        type: String
    ): List<LibraryEntity>

    @Query(
        "SELECT * FROM libraries WHERE name = :name LIMIT 1"
    )
    suspend fun getLibraryByName(
        name: String
    ): LibraryEntity?

    @Query(
        "DELETE FROM libraries WHERE id = :id"
    )
    suspend fun deleteLibrary(
        id: Long
    )

    @Query(
        """
    SELECT * FROM series
    WHERE libraryId = :libraryId
    """
    )
    suspend fun getSeriesForLibrary(
        libraryId: Long
    ): List<SeriesEntity>


    @Query(
        """ SELECT * FROM episodes WHERE seriesId = :seriesId ORDER BY episodeNumber
    """
    )
    suspend fun getEpisodesForSeries(
        seriesId: Long
    ): List<EpisodeEntity>


    @Query(
        """ SELECT * FROM artworks WHERE seriesId = :seriesId """
    )
    suspend fun getArtworksForSeries(
        seriesId: Long
    ): List<ArtworkEntity>

    //^^Library ====================================================================================

    @Insert
    suspend fun insertSeries(
        series: SeriesEntity
    ): Long

    @Query(
        "SELECT * FROM series"
    )
    suspend fun getAllSeries():
            List<SeriesEntity>


    @Transaction
    @Query(
        "SELECT * FROM series WHERE id = :id"
    )
    suspend fun getSeriesComplete(
        id: Long
    ): SeriesComplete?


    @Query(
        """
    SELECT * FROM series
    WHERE title = :title
    LIMIT 1
    """
    )
    suspend fun getSeriesByTitle(
        title: String
    ): SeriesEntity?

    @Query(
        "SELECT * FROM series WHERE id = :id LIMIT 1"
    )
    suspend fun getSeriesById(
        id: Long
    ): SeriesEntity?

    @Query(
        """
    SELECT * FROM series
    ORDER BY addedDate DESC
    LIMIT :limit
    """
    )
    suspend fun getRecentlyAddedSeries(
        limit: Int
    ): List<SeriesEntity>


    @Query(
        """
    SELECT * FROM playback_history
    ORDER BY lastPlayed DESC
    """
    )
    suspend fun getPlaybackHistory():
            List<PlaybackHistoryEntity>


    @Query(
        """
    SELECT * FROM playback_history
    WHERE position > 0
    ORDER BY lastPlayed DESC
    """
    )
    suspend fun getContinueWatching():
            List<PlaybackHistoryEntity>


    @Query(""" UPDATE series SET tmdbId = :tmdbId WHERE id = :seriesId """)
    suspend fun updateTmdbId(
        seriesId: Long,
        tmdbId: Int
    )

    @Query("""
    DELETE FROM episodes
    WHERE seriesId = :seriesId
""")
    suspend fun deleteEpisodesBySeriesId(seriesId: Long)


    @Query("""
    SELECT * FROM series
    WHERE libraryId = :libraryId
""")
    suspend fun getSeriesByLibraryId(
        libraryId: Long
    ): List<SeriesEntity>

    @Query("""
    SELECT * FROM movies
    WHERE libraryId = :libraryId
""")
    suspend fun getMoviesByLibraryId(
        libraryId: Long
    ): List<MovieEntity>

    @Query("""
    DELETE FROM series
    WHERE id = :seriesId
""")
    suspend fun deleteSeriesById(
        seriesId: Long
    )

    @Query("""
    DELETE FROM movies
    WHERE id = :movieId
""")
    suspend fun deleteMovieById(
        movieId: Long
    )
    //^^Series =====================================================================================


    @Insert
    suspend fun insertMovie(
        movie: MovieEntity
    ): Long

    @Update
    suspend fun updateMovie(
        movie: MovieEntity
    )

    @Delete
    suspend fun deleteMovie(
        movie: MovieEntity
    )

    @Query(
        """ SELECT * FROM movies WHERE title=:title LIMIT 1 """
    )
    suspend fun getMovieByTitle(
        title: String
    ): MovieEntity?

    @Query(
        """ SELECT * FROM movies WHERE id=:id """
    )
    suspend fun getMovieById(
        id: Long
    ): MovieEntity?

    @Query(
        """ SELECT * FROM movies WHERE libraryId=:libraryId ORDER BY title """
    )
    suspend fun getMovies(
        libraryId: Long
    ): List<MovieEntity>


    @Query(
        """
    UPDATE movies
    SET tmdbId = :tmdbId
    WHERE id = :movieId
    """
    )
    suspend fun updateMovieTmdbId(

        movieId: Long,

        tmdbId: Int

    )

    @Query(
        """
    UPDATE movies
    SET metadataFolder = :folder
    WHERE id = :movieId
    """
    )
    suspend fun updateMovieMetadataFolder(

        movieId: Long,

        folder: String

    )


    //^^Movies =====================================================================================

    @Insert
    suspend fun insertMusic(
        music: MusicEntity
    ):Long

    @Update
    suspend fun updateMusic(
        music:MusicEntity)

    @Delete
    suspend fun deleteMusic(
        music:MusicEntity)

    @Query(""" SELECT * FROM music WHERE title=:title LIMIT 1 """)
    suspend fun getMusicByTitle(
        title:String
    ):MusicEntity?

    @Query(""" SELECT * FROM music WHERE libraryId=:libraryId ORDER BY artist,title """)
    suspend fun getMusic(
        libraryId:Long
    ):List<MusicEntity>

    //^^Music ======================================================================================


    @Insert
    suspend fun insertEpisode(
        episode: EpisodeEntity
    )


    @Query(
        """ UPDATE series SET episodeCount = :count, lastScan = :lastScan WHERE id = :seriesId """
    )
    suspend fun updateEpisodeCount(
        seriesId: Long,
        count: Int,
        lastScan: Long
    )

    @Query(
        """ SELECT COUNT(*) FROM episodes WHERE seriesId = :seriesId """
    )
    suspend fun getEpisodeCount(
        seriesId: Long
    ): Int


    @Query(
        """ SELECT * FROM playback_history WHERE mediaId IN
            ( SELECT id FROM episodes WHERE seriesId = :seriesId)
                ORDER BY lastPlayed DESC LIMIT 1 """
    )
    suspend fun getLatestPlaybackForSeries(
        seriesId: Long
    ): PlaybackHistoryEntity?

    @Query(""" SELECT * FROM episodes 
                        WHERE seriesId = :seriesId 
                        AND seasonNumber = :seasonNumber 
                        AND episodeNumber = :episodeNumber LIMIT 1
                  """)
    suspend fun getEpisode(
        seriesId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodeEntity?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayback(
        playback: PlaybackHistoryEntity
    )

    @Update
    suspend fun updatePlayback(
        playback: PlaybackHistoryEntity
    )

    @Query("""
SELECT * FROM playback_history
WHERE mediaId = :episodeId
LIMIT 1
""")
    suspend fun getEpisodePlayback(
        episodeId: Long
    ): PlaybackHistoryEntity?

    @Query("""
DELETE FROM playback_history
WHERE mediaId = :episodeId
""")
    suspend fun deleteEpisodePlayback(
        episodeId: Long
    )


    @Query(""" SELECT * FROM playback_history WHERE mediaId IN 
                            ( SELECT id FROM episodes WHERE seriesId = :seriesId)
                                    ORDER BY lastPlayed DESC
                            LIMIT 1
""")
    suspend fun getLastPlayedEpisodeForSeries(
        seriesId: Long
    ): PlaybackHistoryEntity?

    //^^Episode=====================================================================================


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(
        artwork: ArtworkEntity
    )


    @Query(
        """ SELECT * FROM artworks WHERE seriesId = :seriesId AND type = :type LIMIT 1"""
    )
    suspend fun getArtwork(
        seriesId: Long,
        type: String
    ): ArtworkEntity?

    @Query(
        """ SELECT * FROM artworks WHERE seriesId = :seriesId """
    )
    suspend fun getAllArtwork(
        seriesId: Long
    ): List<ArtworkEntity>


    @Query(""" SELECT *  FROM artworks WHERE seriesId=:seriesId """)
    suspend fun getArtworkForSeries(
        seriesId: Long
    ): List<ArtworkEntity>

    @Query(
        """ DELETE FROM artworks WHERE seriesId = :seriesId AND type = :type""")
    suspend fun deleteArtwork(
        seriesId: Long,
        type: String
    )


    //^^Artwork=====================================================================================


    @Query(
        """
    UPDATE series
    SET metadataFolder = :folder
    WHERE id = :seriesId
    """
    )
    suspend fun updateMetadataFolder(
        seriesId: Long,
        folder: String
    )


    //^^MetaData====================================================================================



















    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieArtwork(
        artwork: MovieArtworkEntity
    )

    @Query(
        """
    SELECT * FROM movie_artwork
    WHERE movieId = :movieId
    AND type = :type
    LIMIT 1
    """
    )
    suspend fun getMovieArtwork(
        movieId: Long,
        type: String
    ): MovieArtworkEntity?

    @Query(
        """
    SELECT * FROM movie_artwork
    WHERE movieId = :movieId
    """
    )
    suspend fun getMovieArtworkList(
        movieId: Long
    ): List<MovieArtworkEntity>

    @Query(
        """
    DELETE FROM movie_artwork
    WHERE movieId = :movieId
    AND type = :type
    """
    )
    suspend fun deleteMovieArtwork(
        movieId: Long,
        type: String
    )


    @Query("""
SELECT * FROM movie_artwork
WHERE movieId = :movieId
""")
    suspend fun getMovieArtwork(
        movieId: Long
    ): List<MovieArtworkEntity>


    @Query("""
SELECT * FROM movies
ORDER BY addedDate DESC
LIMIT :limit
""")
    suspend fun getRecentlyAddedMovies(
        limit: Int
    ): List<MovieEntity>


    @Query("""
SELECT * FROM movie_playback
ORDER BY lastPlayed DESC
""")
    suspend fun getMoviePlaybackHistory():
            List<MoviePlaybackEntity>


    @Query("""
SELECT * FROM movie_playback
WHERE position > 0
ORDER BY lastPlayed DESC
""")
    suspend fun getMovieContinueWatching():
            List<MoviePlaybackEntity>



    //^^Movie Artwork===============================================================================




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePlayback(
        playback: MoviePlaybackEntity
    )

    @Update
    suspend fun updateMoviePlayback(
        playback: MoviePlaybackEntity
    )

    @Query(
        """
    SELECT * FROM movie_playback
    WHERE movieId = :movieId
    LIMIT 1
    """
    )
    suspend fun getMoviePlayback(
        movieId: Long
    ): MoviePlaybackEntity?

    @Query(
        """
    SELECT * FROM movie_playback
    WHERE position > 0
    ORDER BY lastPlayed DESC
    """
    )
    suspend fun getContinueWatchingMovies():
            List<MoviePlaybackEntity>

    @Query(
        """
    DELETE FROM movie_playback
    WHERE movieId = :movieId
    """
    )
    suspend fun deleteMoviePlayback(
        movieId: Long
    )
    //^^Movie Playback===============================================================================











    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusicArtwork(
        artwork: MusicArtworkEntity
    )

    @Query(
        """
    SELECT * FROM music_artwork
    WHERE musicId = :musicId
    AND type = :type
    LIMIT 1
    """
    )
    suspend fun getMusicArtwork(
        musicId: Long,
        type: String
    ): MusicArtworkEntity?

    @Query(
        """
    SELECT * FROM music_artwork
    WHERE musicId = :musicId
    """
    )
    suspend fun getMusicArtworkList(
        musicId: Long
    ): List<MusicArtworkEntity>

    @Query(
        """
    DELETE FROM music_artwork
    WHERE musicId = :musicId
    AND type = :type
    """
    )
    suspend fun deleteMusicArtwork(
        musicId: Long,
        type: String
    )

    //^^Music Artwork===============================================================================




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusicPlayback(
        playback: MusicPlaybackEntity
    )

    @Update
    suspend fun updateMusicPlayback(
        playback: MusicPlaybackEntity
    )

    @Query(
        """
    SELECT * FROM music_playback
    WHERE musicId = :musicId
    LIMIT 1
    """
    )
    suspend fun getMusicPlayback(
        musicId: Long
    ): MusicPlaybackEntity?

    @Query(
        """
    SELECT * FROM music_playback
    ORDER BY lastPlayed DESC
    """
    )
    suspend fun getMusicHistory():
            List<MusicPlaybackEntity>

    @Query(
        """
    DELETE FROM music_playback
    WHERE musicId = :musicId
    """
    )
    suspend fun deleteMusicPlayback(
        musicId: Long
    )

    //^^Music Playback===============================================================================



}