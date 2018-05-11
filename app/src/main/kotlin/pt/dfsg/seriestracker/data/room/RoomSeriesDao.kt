package pt.dfsg.seriestracker.data.room

import android.arch.persistence.room.*
import io.reactivex.Flowable
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show


@Dao
@TypeConverters(RoomConverters::class)
interface RoomSeriesDao {
    @Query("SELECT * FROM show")
    fun getAllShows(): Flowable<List<Show>>

    @Query("SELECT * FROM show WHERE id = :id")
    fun getShowById(id: Int): Flowable<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShow(show: Show)

    @Delete
    fun deleteShow(show: Show)

    @Update
    fun update(show: Show)

    @Query("SELECT * FROM season WHERE id = :id")
    fun getSeason(id: Long): Flowable<List<Season>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeason(season: Season)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSeasons(seasons: List<Season>)

    @Query("SELECT * FROM episode WHERE id = :id")
    fun getEpisode(id: Long): Flowable<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisode(episode: Episode)


}