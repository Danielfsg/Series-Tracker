package pt.dfsg.seriestracker.data.repository

import android.arch.lifecycle.LiveData
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Search
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show

interface Repository {

    fun getShowsFromRoom(): LiveData<List<Show>>

    fun addShowAsync(show: Show)

    fun deleteShowAsync(show: Show)

    fun searchShowFromRemote(query: String): LiveData<List<Search>>

    fun getSeasonsFromRoom(showId: Long): LiveData<List<Season>>

    fun getSeasonsFromRemote(show: Show): LiveData<List<Season>>

    //fun deleteSeasonAsync(season: Season)

    fun getEpisodesFromRoom(showId: Long): LiveData<List<Episode>>

    //fun getEpisodesBySeasonIdFromRemote(seasonId: Long): LiveData<List<Episode>>

    fun getEpisodesByShowIdFromRemote(show: Show, update: Boolean): LiveData<List<Episode>>

    fun updateEpisodeAsync(episode: Episode)

    //fun deleteEpisodeAsync(episode: Episode)

}