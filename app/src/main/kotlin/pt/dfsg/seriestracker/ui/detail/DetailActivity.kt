package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_detail.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.utils.load
import pt.dfsg.seriestracker.utils.toast
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    private lateinit var detailsViewModel: DetailsViewModel

    lateinit var show: Show

    var seasonList: List<Season>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        show = intent.extras.get("SHOW") as Show

        supportActionBar?.title = show.name
    }

    override fun onStart() {
        super.onStart()

        initViewModel()
        initViewsWithData()

        getSeason()

    }

    private fun initViewsWithData() {
        show_name.text = show.name
        show_premiered.text = show.premiered
        show_channel.text = show.network?.name ?: show.webChannel?.name
        show_runtime.text = String.format("${show.runtime.toString()} minutes")
        show_status.text = show.status
        show_rating.text = show.rating?.average.toString()
        image_view.load(show.image?.medium.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            show_summary.text = Html.fromHtml(show.summary, Html.FROM_HTML_MODE_COMPACT)
        } else {
            show_summary.text = Html.fromHtml(show.summary)
        }
    }

    private fun initViewModel() {
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        detailsViewModel.let { lifecycle.addObserver(it) }
    }

    private fun setFavoriteIcon(item: MenuItem) {
        item.icon = if (show.isFavorite)
            ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
        else
            ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
    }

    private fun saveFavorite(show: Show) {
        if (show.isFavorite) {
            detailsViewModel.addShow(show)
            toast("${show.name} Added to Favorites")
        } else {
            detailsViewModel.delete(show)
            toast("${show.name} Removed from Favorites")
        }
    }

    private fun getSeason() {
        detailsViewModel.getSeasons(show.id)?.observe(this, Observer {
            seasonList = it
            Timber.d(seasonList?.joinToString(", "))
            toast("size: ${it?.size}")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_favorites, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.let { setFavoriteIcon(it) }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_favorites -> {
                show.isFavorite = !show.isFavorite
                setFavoriteIcon(item)
                saveFavorite(show)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
