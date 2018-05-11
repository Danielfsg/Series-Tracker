package pt.dfsg.seriestracker.data.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show


@Database(
    entities = [(Show::class), (Season::class), (Episode::class)],
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

        /**
         * Example of room migration
         * more at : https://github.com/googlesamples/android-architecture-components/blob/master/PersistenceMigrationsSample/app/src/room3/java/com/example/android/persistence/migrations/UsersDatabase.java
         * Migrate from:
         * version 2 - using Room
         * to
         * version 3 - using Room where the model has an extra field
         */
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users " + " ADD COLUMN last_update INTEGER")
            }
        }

    }


}