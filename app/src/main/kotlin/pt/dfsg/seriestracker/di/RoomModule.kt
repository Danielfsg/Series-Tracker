package pt.dfsg.seriestracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import pt.dfsg.seriestracker.data.room.RoomSeriesDataSource
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomSeriesDataSource(context: Context) =
        RoomSeriesDataSource.getDatabase(context)
}