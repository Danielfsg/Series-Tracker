package pt.dfsg.seriestracker.ui.favorites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_list.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.ui.main.SeriesViewModel
import pt.dfsg.seriestracker.ui.detail.DetailActivity


class FavoritesFragment : Fragment(), FavoritesAdapter.ClickCallBack {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private var seriesViewModel: SeriesViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onStart() {
        super.onStart()

        initAdapter()
        initViewModel()
        getFavorites()
    }

    private fun initAdapter() {
        favoritesAdapter = FavoritesAdapter(this)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = favoritesAdapter
    }

    private fun initViewModel() {
        seriesViewModel = ViewModelProviders.of(this).get(SeriesViewModel::class.java)
        seriesViewModel?.let { lifecycle.addObserver(it) }
    }

    private fun getFavorites() {
        seriesViewModel?.getFavorites()
            ?.observe(this, Observer { fav -> fav?.let { favoritesAdapter.setData(it) } })
    }


    override fun onItemClick(show: Show) {
        startActivity(
            Intent(context, DetailActivity::class.java)
                .apply {
                    putExtra("SHOW", show)
                }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }
}
