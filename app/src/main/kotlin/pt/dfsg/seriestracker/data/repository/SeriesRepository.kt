package pt.dfsg.seriestracker.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.async
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.data.remote.RemoteSeriesDataSource
import pt.dfsg.seriestracker.data.room.RoomSeriesDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeriesRepository @Inject constructor(
    private val roomSeriesDataSource: RoomSeriesDataSource,
    private val remoteSeriesDataSource: RemoteSeriesDataSource
) : Repository {

    val allCompositeDisposable: MutableList<Disposable> = arrayListOf()

    override fun getShowFromDB(): LiveData<List<Show>> {
        val roomSeriesDao = roomSeriesDataSource.showDao()
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

    override fun searchShow(query: String): LiveData<List<Search>> {
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


}