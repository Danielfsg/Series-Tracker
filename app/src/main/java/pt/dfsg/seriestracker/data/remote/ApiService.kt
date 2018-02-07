package pt.dfsg.seriestracker.data.remote

import io.reactivex.Observable
import pt.dfsg.seriestracker.data.model.Search
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Endpoints.SEARCH)
    fun search(@Query("q") query: String): Observable<List<Search>>


    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Endpoints.ROOT_URl)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
