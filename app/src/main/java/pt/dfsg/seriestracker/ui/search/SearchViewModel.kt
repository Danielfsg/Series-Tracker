package pt.dfsg.seriestracker.ui.search

import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import pt.dfsg.seriestracker.data.model.Model
import pt.dfsg.seriestracker.data.remote.Repository


//class SearchViewModel(private val repository: Repository) : ViewModel() {
//
//    fun getShowRx(query: String): Flowable<List<Model.Search>> {
//        return repository.fetchShowRx(query)
//    }
//
//
//}