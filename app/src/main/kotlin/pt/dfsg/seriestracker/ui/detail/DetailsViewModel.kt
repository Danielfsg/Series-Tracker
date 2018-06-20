package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import pt.dfsg.seriestracker.SeriesApplication
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.data.repository.SeriesRepository
import javax.inject.Inject


class DetailsViewModel : ViewModel(), LifecycleObserver {

    @Inject
    lateinit var seriesRepository: SeriesRepository

    private val compositeDisposable = CompositeDisposable()

    private val resultSeason: MediatorLiveData<List<Season>> = MediatorLiveData()
    private val resultEpisode: MediatorLiveData<List<Episode>> = MediatorLiveData()

    private lateinit var show: Show

    init {
        initializeDagger()
    }

    fun addShow(show: Show) {
        seriesRepository.addShowAsync(show)
    }

    fun delete(show: Show) {
        seriesRepository.deleteShowAsync(show)
    }

    fun getShow(): Show = show

    fun setShow(show: Show) {
        this.show = show
    }

    fun getSeasons(): MediatorLiveData<List<Season>>? {
        val dbSource = seriesRepository.getSeasonsFromRoom(show.id)
        resultSeason.addSource(dbSource) { data ->
            resultSeason.removeSource(dbSource)
            if (data != null && data.isNotEmpty()) {
                resultSeason.value = data
            } else {
                val apiSource = seriesRepository.getSeasonsFromRemote(show)
                resultSeason.addSource(apiSource) { newData ->
                    resultSeason.removeSource(apiSource)
                    if (newData != null && newData.isNotEmpty()) {
                        resultSeason.value = newData
                    }
                }
            }
        }
        return resultSeason
    }

    fun getEpisodes(): MediatorLiveData<List<Episode>>? {
        val dbSource = seriesRepository.getEpisodesFromRoom(show.id)
        resultEpisode.addSource(dbSource) { data ->
            resultEpisode.removeSource(dbSource)
            if (data != null && data.isNotEmpty()) {
                resultEpisode.value = data
            } else {
                val apiSource = seriesRepository.getEpisodesByShowIdFromRemote(show, false)
                resultEpisode.addSource(apiSource) { newData ->
                    resultEpisode.value = newData
                    resultEpisode.removeSource(apiSource)
                    if (newData != null && newData.isNotEmpty()) {
                        resultEpisode.value = newData
                    }
                }
            }
        }
        return resultEpisode
    }

    fun updateEpisode(episode: Episode) {
        seriesRepository.updateEpisodeAsync(episode)
    }

    fun refreshShowInfo() {
        val apiSource = seriesRepository.getEpisodesByShowIdFromRemote(show, false)
        resultEpisode.addSource(apiSource) { newData ->
            resultEpisode.value = newData
            resultEpisode.removeSource(apiSource)
            if (newData != null && newData.isNotEmpty()) {
                resultEpisode.value = newData
            }
        }
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