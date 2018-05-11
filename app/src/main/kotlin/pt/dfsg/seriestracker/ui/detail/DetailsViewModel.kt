package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import pt.dfsg.seriestracker.SeriesApplication
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.data.repository.SeriesRepository
import javax.inject.Inject


class DetailsViewModel : ViewModel(), LifecycleObserver {

    @Inject
    lateinit var seriesRepository: SeriesRepository

    private var liveSeasonList: MutableLiveData<List<Season>>? = null

    private val liveDataMerger: MediatorLiveData<List<Season>> = MediatorLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        initializeDagger()
    }

    fun addShow(show: Show) {
        seriesRepository.addShow(show)
    }

    fun delete(show: Show) {
        seriesRepository.deleteShow(show)
    }

    fun getSeasons(showId: Long): MediatorLiveData<List<Season>>? {

        liveDataMerger.addSource(
            seriesRepository.getSeasonsFromRoom(showId),
            { list -> liveDataMerger.value = list })
        liveDataMerger.addSource(
            seriesRepository.getSeasonsFromRemote(showId),
            { list -> liveDataMerger.value = list })

        return liveDataMerger
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unSubscribeViewModel() {
        for (disposable in seriesRepository.allCompositeDisposable) {
            compositeDisposable.addAll(disposable)
        }
        compositeDisposable.clear()
    }

    override fun onCleared() {
        unSubscribeViewModel()
        super.onCleared()
    }


    private fun initializeDagger() = SeriesApplication.appComponent.inject(this)


}