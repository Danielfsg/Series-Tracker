package pt.dfsg.seriestracker.di

import dagger.Component
import pt.dfsg.seriestracker.ui.detail.DetailsViewModel
import pt.dfsg.seriestracker.ui.main.SeriesViewModel
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AppModule::class), (RemoteModule::class), (RoomModule::class), (PicassoModule::class)]
)

interface AppComponent {
    fun inject(seriesViewModel: SeriesViewModel)
    fun inject(detailsViewModel: DetailsViewModel)
}
