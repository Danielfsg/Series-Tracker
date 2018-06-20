package pt.dfsg.seriestracker.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.async
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.data.remote.RemoteSeriesDataSource
import pt.dfsg.seriestracker.data.room.RoomSeriesDataSource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeriesRepository @Inject constructor(
    private val roomSeriesDataSource: RoomSeriesDataSource,
    private val remoteSeriesDataSource: RemoteSeriesDataSource
) : Repository {

    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    private val roomSeriesDao = roomSeriesDataSource.showDao()

    //------------------------------------
    // Shows
    //------------------------------------

    override fun searchShowFromRemote(query: String): LiveData<List<Search>> {
        val mutableLiveData = MutableLiveData<List<Search>>()
        val disposable = remoteSeriesDataSource.search(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} search list from Remote...")
                },
                { Timber.d(it) })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun getShowsFromRoom(): LiveData<List<Show>> {
        val mutableLiveData = MutableLiveData<List<Show>>()
        val disposable = roomSeriesDao.getAllShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} shows list from Room...")
                },
                { Timber.d(it) })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun addShowAsync(show: Show) {
        async { roomSeriesDataSource.showDao().insertShow(show) }
    }


    override fun deleteShowAsync(show: Show) {
        async {
            roomSeriesDataSource.showDao().deleteShow(show)
            roomSeriesDataSource.showDao().deleteSeasonByShowId(show.id)
            roomSeriesDataSource.showDao().deleteEpisodeByShowId(show.id)
        }
    }

    //------------------------------------
    // Seasons
    //------------------------------------

    override fun getSeasonsFromRoom(showId: Long): LiveData<List<Season>> {
        val roomSeriesDao = roomSeriesDataSource.showDao()
        val mutableLiveData = MutableLiveData<List<Season>>()
        val disposable = roomSeriesDao.getSeasonByShowId(showId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} Seasons from Room...")
                },
                { Timber.d(it) })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun getSeasonsFromRemote(show: Show): LiveData<List<Season>> {
        val mutableLiveData = MutableLiveData<List<Season>>()
        val disposable = remoteSeriesDataSource.season(show.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (show.isFavorite)
                    storeSeasonInRoom(show.id, it)
            }
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} Seasons from Remote...")
                },
                { Timber.d(it) }
            )

        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    private fun storeSeasonInRoom(showId: Long, seasonList: List<Season>) {
        Observable.fromCallable {
            seasonList.forEach {
                it.id_show = showId
                roomSeriesDao.insertSeason(it)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Timber.d("Inserted ${seasonList.size} Seasons from Remote in Room...")
            }
    }

    //------------------------------------
    // Episodes
    //------------------------------------

    override fun getEpisodesFromRoom(showId: Long): LiveData<List<Episode>> {
        val roomSeriesDao = roomSeriesDataSource.showDao()
        val mutableLiveData = MutableLiveData<List<Episode>>()
        val disposable = roomSeriesDao.getEpisodeByShowId(showId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} Episodes from Room...")
                },
                { Timber.d(it) })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun getEpisodesByShowIdFromRemote(show: Show, update: Boolean): LiveData<List<Episode>> {
        val mutableLiveData = MutableLiveData<List<Episode>>()
        val disposable = remoteSeriesDataSource.getEpisodeByShow(show.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (show.isFavorite)
                    if (update)
                        updateEpisodesAsync(show.id, it)
                    else
                        storeEpisodeInRoom(show.id, it)
            }
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} EpisodesByShowId from Remote...")
                },
                { Timber.d(it) }
            )

        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun updateEpisodeAsync(episode: Episode) {
        async { roomSeriesDataSource.showDao().updateEpisode(episode) }
    }

    private fun updateEpisodesAsync(showId: Long, episodeList: List<Episode>) {
        async {
            episodeList.forEach {
                it.id_show = showId
                it.isOpen = false
                roomSeriesDataSource.showDao().updateEpisode(it)
            }
        }
    }

    private fun storeEpisodeInRoom(showId: Long, episodeList: List<Episode>) {
        Observable.fromCallable {
            episodeList.forEach {
                it.id_show = showId
                it.isOpen = false
                roomSeriesDataSource.showDao().insertEpisode(it)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Timber.d("Inserted ${episodeList.size} Episodes from Remote in Room...")
            }
    }

}