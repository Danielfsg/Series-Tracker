package pt.dfsg.seriestracker.ui.main

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.data.repository.SeriesRepository
import pt.dfsg.seriestracker.SeriesApplication
import javax.inject.Inject

class SeriesViewModel : ViewModel(), LifecycleObserver {

    @Inject
    lateinit var seriesRepository: SeriesRepository

    private val compositeDisposable = CompositeDisposable()
    private var liveSearchList: LiveData<List<Search>>? = null
    private var liveShowList: LiveData<List<Show>>? = null

    init {
        initializeDagger()
    }

    fun searchShow(query: String): LiveData<List<Search>>? {
        liveSearchList = null
        liveSearchList = MutableLiveData<List<Search>>()
        liveSearchList = seriesRepository.searchShowFromRemote(query)
        return liveSearchList
    }

    fun getFavorites(): LiveData<List<Show>>? {
        liveShowList = null
        liveShowList = MutableLiveData<List<Show>>()
        liveShowList = seriesRepository.getShowsFromRoom()
        return liveShowList
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