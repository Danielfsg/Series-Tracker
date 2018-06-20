package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_detail.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.utils.toast


class DetailActivity : AppCompatActivity() {
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mMainPagerAdapter: DetailPagerAdapter
    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        show = intent.extras.get("SHOW") as Show

        supportActionBar?.title = show.name

        initViewModel()
    }

    private fun initViewModel() {
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        detailsViewModel.let { lifecycle.addObserver(it) }
        detailsViewModel.setShow(show)
        detailsViewModel.getSeasons()?.observe(this, Observer { initViewpager(it) })
    }

    private fun initViewpager(list: List<Season>?) {
        if (list != null) {
            val filteredList = list.filter { !it.premiereDate.isNullOrEmpty() }.toList()
            mMainPagerAdapter = DetailPagerAdapter(this, detailsViewModel, supportFragmentManager, filteredList)
            container.adapter = mMainPagerAdapter
            container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
            tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
            (0..filteredList.count()).forEach {
                tabs.addTab(tabs.newTab().setText(mMainPagerAdapter.getPageTitle(it)))
            }
            detailsViewModel.getSeasons()?.removeObservers(this)
        }
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
            toast("${show.name} Added to Favorites.")
        } else {
            detailsViewModel.delete(show)
            toast("${show.name} Removed from Favorites.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_favorites, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(1)?.let { setFavoriteIcon(it) }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_update -> {
                //TODO:
                return true
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
