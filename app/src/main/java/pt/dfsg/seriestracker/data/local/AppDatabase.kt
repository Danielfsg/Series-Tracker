package pt.dfsg.seriestracker.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import pt.dfsg.seriestracker.data.model.Show

@Database(entities = [(Show::class)], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    companion object {
        private var dbInstance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (dbInstance == null) {
                dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db-shows"
                ).fallbackToDestructiveMigration().build()
            }
            return dbInstance
        }
    }
}