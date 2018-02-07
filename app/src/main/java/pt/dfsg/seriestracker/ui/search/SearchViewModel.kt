package pt.dfsg.seriestracker.ui.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Observable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import pt.dfsg.seriestracker.data.local.AppDatabase
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.data.remote.RepositoryProvider


class SearchViewModel constructor(app: Application) : AndroidViewModel(app) {

    private var appDatabase: AppDatabase? = null

    private val repository = RepositoryProvider.provideSearchRepository()

    init {
        appDatabase = AppDatabase.getDatabase(app)
    }

    fun getShowRx(query: String): Observable<List<Search>> {
        return repository.fetchShowRx(query)
    }

    fun addShow(show: Show) {
        async(CommonPool) { appDatabase?.showDao()?.insertNote(show) }
    }

}