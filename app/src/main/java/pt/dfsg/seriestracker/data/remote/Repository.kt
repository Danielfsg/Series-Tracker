package pt.dfsg.seriestracker.data.remote

import io.reactivex.Observable
import pt.dfsg.seriestracker.data.model.Model

class Repository(private val apiService: ApiService) {

    fun fetchShowRx(query: String): Observable<List<Model.Search>> {
        return apiService.search(query)
    }

}