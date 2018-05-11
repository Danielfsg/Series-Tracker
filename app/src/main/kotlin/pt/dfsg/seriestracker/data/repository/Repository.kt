package pt.dfsg.seriestracker.data.repository

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show

interface Repository {

    fun getShowsFromRoom(): LiveData<List<Show>>

    fun addShow(show: Show)

    fun deleteShow(show: Show)

    fun searchShowFromRemote(query: String): LiveData<List<Search>>

    fun getSeasonsFromRoom(showId: Long): LiveData<List<Season>>

    fun getSeasonsFromRemote(showId: Long): LiveData<List<Season>>

}