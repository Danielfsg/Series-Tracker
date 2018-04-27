package pt.dfsg.seriestracker.di

import dagger.Component
import pt.dfsg.seriestracker.ui.SeriesViewModel
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AppModule::class), (RemoteModule::class), (RoomModule::class)]
)

interface AppComponent {
    fun inject(seriesViewModel: SeriesViewModel)
    //fun inject(favoritesViewModel: SeriesViewModel)
}
