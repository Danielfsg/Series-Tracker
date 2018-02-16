package pt.dfsg.seriestracker.ui.favorites

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import pt.dfsg.seriestracker.data.local.AppDatabase
import pt.dfsg.seriestracker.data.model.Show


class FavoritesViewModel constructor(app: Application) : AndroidViewModel(app) {

    private var appDatabase: AppDatabase? = null

    private var favoritesList: LiveData<List<Show>>? = null

    init {
        appDatabase = AppDatabase.getDatabase(app)
        favoritesList = appDatabase?.showDao()?.getAllShows()
    }

    fun getFavorites(): LiveData<List<Show>>? {
        return favoritesList
    }

}