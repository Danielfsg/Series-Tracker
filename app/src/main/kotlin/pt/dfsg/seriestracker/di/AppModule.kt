package pt.dfsg.seriestracker.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pt.dfsg.seriestracker.SeriesApplication
import javax.inject.Singleton

@Module
class AppModule(private val seriesApplication: SeriesApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context = seriesApplication

    @Provides
    @Singleton
    fun provideApplication(): Application = seriesApplication

}