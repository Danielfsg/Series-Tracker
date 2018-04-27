package pt.dfsg.seriestracker.data.repository

import android.arch.lifecycle.LiveData
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Show

interface Repository {

    fun getShowFromDB(): LiveData<List<Show>>

    fun addShow(show: Show)

    fun searchShow(query: String): LiveData<List<Search>>

}