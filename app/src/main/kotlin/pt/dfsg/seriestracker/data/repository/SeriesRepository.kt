package pt.dfsg.seriestracker.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.async
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

    /*************************************
     *  Shows
     *************************************/

    override fun searchShowFromRemote(query: String): LiveData<List<Search>> {
        val mutableLiveData = MutableLiveData<List<Search>>()
        val disposable = remoteSeriesDataSource.search(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { searchList -> mutableLiveData.value = searchList },
                { t: Throwable? -> t!!.printStackTrace() })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun getShowsFromRoom(): LiveData<List<Show>> {
        val mutableLiveData = MutableLiveData<List<Show>>()
        val disposable = roomSeriesDao.getAllShows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { showList -> mutableLiveData.value = showList },
                { t: Throwable? -> t!!.printStackTrace() })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    override fun addShow(show: Show) {
        (async { roomSeriesDataSource.showDao().insertShow(show) })
    }


    override fun deleteShow(show: Show) {
        (async { roomSeriesDataSource.showDao().deleteShow(show) })
    }

    /*************************************
     *  Seasons
     *************************************/
/*
    override fun getSeasonsFromRoom(showId: Long): Observable<List<Season>> =
        roomSeriesDao.getSeason(showId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .filter { it.isNotEmpty() }
            .toObservable()
            .doOnNext {
                Timber.d("Dispatching ${it.size} users from Room...")
            }

    override fun getSeasonsFromRemote(showId: Long): Observable<List<Season>> =
        remoteSeriesDataSource.season(showId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext {
                Timber.d("Dispatching ${it.size} users from Remote...")
                storeSeasonInRoom(it)
            }

    */


    override fun getSeasonsFromRoom(showId: Long): LiveData<List<Season>> {
        val roomSeriesDao = roomSeriesDataSource.showDao()
        val mutableLiveData = MutableLiveData<List<Season>>()
        val disposable = roomSeriesDao.getSeason(showId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} items from Room...")
                },
                { Timber.d(it) })
        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }


    override fun getSeasonsFromRemote(showId: Long): LiveData<List<Season>> {
        val mutableLiveData = MutableLiveData<List<Season>>()
        val disposable = remoteSeriesDataSource.season(showId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                storeSeasonInRoom(it)
            }
            .subscribe(
                {
                    mutableLiveData.value = it
                    Timber.d("Dispatching ${it.size} items from Remote...")
                },
                { Timber.d(it) }
            )

        allCompositeDisposable.add(disposable)
        return mutableLiveData
    }

    private fun storeSeasonInRoom(seasonList: List<Season>) {
        Observable.fromCallable { roomSeriesDao.insertAllSeasons(seasonList) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Timber.d("Inserted ${seasonList.size} items from Remote in Room...")
            }
    }

}