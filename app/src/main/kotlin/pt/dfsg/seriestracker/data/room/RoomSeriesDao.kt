package pt.dfsg.seriestracker.data.room

import android.arch.persistence.room.*
import io.reactivex.Flowable
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show


@Dao
@TypeConverters(RoomConverters::class)
interface RoomSeriesDao {

    //------------------------------------
    // Shows
    //------------------------------------

    @Query("SELECT * FROM show")
    fun getAllShows(): Flowable<List<Show>>

    @Query("SELECT * FROM show WHERE id = :id")
    fun getShowById(id: Long): Flowable<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShow(show: Show)

    @Update
    fun updateShow(show: Show)

    @Delete
    fun deleteShow(show: Show)

    @Query("DELETE FROM show WHERE id = :id")
    fun deleteShowById(id: Long)

    //------------------------------------
    // Seasons
    //------------------------------------

    @Query("SELECT * FROM season WHERE id_show = :id")
    fun getSeasonByShowId(id: Long): Flowable<List<Season>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeason(season: Season)

    @Update
    fun updateSeason(season: Season)

    @Delete
    fun deleteSeason(season: Season)

    @Query("DELETE FROM season WHERE id_show = :id")
    fun deleteSeasonByShowId(id: Long)

    //------------------------------------
    // Episodes
    //------------------------------------

    @Query("SELECT * FROM episode WHERE id_show = :id")
    fun getEpisodeByShowId(id: Long): Flowable<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisode(episode: Episode)

    @Update
    fun updateEpisode(episode: Episode)

    @Delete
    fun deleteEpisode(episode: Episode)

    @Query("DELETE FROM episode WHERE id_show = :id")
    fun deleteEpisodeByShowId(id: Long)


}