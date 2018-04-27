package pt.dfsg.seriestracker.data.remote

import io.reactivex.Flowable
import pt.dfsg.seriestracker.data.model.Search
import javax.inject.Inject

class RemoteSeriesDataSource @Inject constructor(private val remoteSeriesService: RemoteSeriesService) {

    fun search(query: String): Flowable<List<Search>> = remoteSeriesService.search(query)
}