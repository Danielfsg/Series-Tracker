package pt.dfsg.seriestracker.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import pt.dfsg.seriestracker.data.model.Show


@Dao
@TypeConverters(ListStringConverter::class)
interface ShowDao {
    @Query("SELECT * FROM Show")
    fun getAllShows(): LiveData<List<Show>>

    @Query("SELECT * FROM Show WHERE id = :id")
    fun getShowById(id: String): LiveData<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(show: Show)

    @Delete
    fun deleteNote(show: Show)

    @Update
    fun update(show: Show)
}