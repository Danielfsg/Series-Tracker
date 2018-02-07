package pt.dfsg.seriestracker.ui

import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.ui.favorites.FavoritesActivity
import pt.dfsg.seriestracker.ui.search.SearchActivity
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


abstract class BaseActivity : AppCompatActivity() {

    fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> throwable.printStackTrace() // TODO()
            is SocketTimeoutException -> throwable.printStackTrace() // handle timeout from Retrofit
            is IOException -> throwable.printStackTrace() // file was not found, do something

        }
    }

    fun mOnNavigationItemSelectedListener() =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(intentFor<FavoritesActivity>().singleTop())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    startActivity(intentFor<SearchActivity>().singleTop())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

}