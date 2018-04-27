package pt.dfsg.seriestracker.data.room

import android.arch.persistence.room.*
import io.reactivex.Flowable
import pt.dfsg.seriestracker.data.model.Show


@Dao
@TypeConverters(RoomConverters::class)
interface RoomSeriesDao {
    @Query("SELECT * FROM Show")
    fun getAllShows(): Flowable<List<Show>>

    @Query("SELECT * FROM Show WHERE id = :id")
    fun getShowById(id: String): Flowable<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShow(show: Show)

    @Delete
    fun deleteShow(show: Show)

    @Update
    fun update(show: Show)
}