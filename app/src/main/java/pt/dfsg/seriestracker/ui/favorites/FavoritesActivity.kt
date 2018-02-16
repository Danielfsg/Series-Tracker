package pt.dfsg.seriestracker.ui.favorites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.content_list.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.ui.BaseActivity
import pt.dfsg.seriestracker.ui.detail.DetailActivity

class FavoritesActivity : BaseActivity(), FavoritesAdapter.ClickCallBack {


    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_favorites)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener())

        favoritesAdapter = FavoritesAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = favoritesAdapter

        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)

        viewModel.getFavorites()?.observe(this,
            Observer { fav -> fav?.let { favoritesAdapter.setData(it) } }
        )
    }

    override fun onItemClick(show: Show) {
        startActivity(
            Intent(this, DetailActivity::class.java)
                .apply {
                    putExtra("SHOW", show)
                }
        )
    }
}
