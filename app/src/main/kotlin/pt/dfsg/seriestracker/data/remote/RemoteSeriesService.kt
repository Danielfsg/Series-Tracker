package pt.dfsg.seriestracker.data.remote

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Season
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteSeriesService {
    @GET(RemoteContract.SEARCH)
    fun search(@Query("q") query: String): Observable<List<Search>>

    @GET(RemoteContract.SEASONS)
    fun season(@Path("showId") showId: Long): Observable<List<Season>>

    @GET(RemoteContract.EPISODES_BY_SEASON)
    fun getEpisodeBySeason(@Path("seasonId") seasonId: Long): Observable<List<Episode>>

    @GET(RemoteContract.EPISODES_BY_SHOW)
    fun getEpisodeByShow(@Path("showId") showId: Long): Observable<List<Episode>>
}