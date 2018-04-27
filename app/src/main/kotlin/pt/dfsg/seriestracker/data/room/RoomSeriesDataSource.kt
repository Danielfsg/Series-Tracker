package pt.dfsg.seriestracker.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import pt.dfsg.seriestracker.data.model.Show

@Database(
    entities = [(Show::class)],
    version = RoomContract.DATABASE_VERSION,
    exportSchema = false
)
abstract class RoomSeriesDataSource : RoomDatabase() {

    abstract fun showDao(): RoomSeriesDao

    companion object {

        fun getDatabase(context: Context): RoomSeriesDataSource =
            Room.databaseBuilder(
                context.applicationContext,
                RoomSeriesDataSource::class.java,
                RoomContract.DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }
}