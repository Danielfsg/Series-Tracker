package pt.dfsg.seriestracker.data.remote

import android.arch.lifecycle.LiveData
import io.reactivex.Flowable
import io.reactivex.Observable
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Season
import javax.inject.Inject

class RemoteSeriesDataSource @Inject constructor(private val remoteSeriesService: RemoteSeriesService) {

    fun search(query: String): Observable<List<Search>> = remoteSeriesService.search(query)

    fun season(showId: Long): Observable<List<Season>> = remoteSeriesService.season(showId)

    fun getEpisodeBySeason(seasonId: Long): Observable<List<Episode>> = remoteSeriesService.getEpisodeBySeason(seasonId)

    fun getEpisodeByShow(seasonId: Long): Observable<List<Episode>> = remoteSeriesService.getEpisodeByShow(seasonId)
}