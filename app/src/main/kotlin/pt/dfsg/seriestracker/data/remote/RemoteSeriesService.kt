package pt.dfsg.seriestracker.data.remote

import io.reactivex.Flowable
import pt.dfsg.seriestracker.data.model.Search
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteSeriesService {
    @GET(RemoteContract.SEARCH)
    fun search(@Query("q") query: String): Flowable<List<Search>>
}