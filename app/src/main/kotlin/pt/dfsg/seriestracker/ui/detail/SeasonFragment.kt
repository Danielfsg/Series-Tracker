package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_season.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Season

class SeasonFragment : Fragment(), SeasonAdapter.ClickCallBack {

    private lateinit var parentActivity: DetailActivity
    private lateinit var detailsViewModel: DetailsViewModel

    private var seasonList: List<Season>? = null
    private var episodeList: List<Episode>? = null
    private var position: Int = 0

    private lateinit var seasonAdapter: SeasonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_season, container, false)
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
        initSeasonInfo()
    }

    private fun initSeasonInfo() {
        val season = seasonList?.get(position - 1)
        val seasonName = if (season?.name.isNullOrEmpty()) "TBD" else season?.name
        season_name.text = seasonName

        val description = season?.summary ?: "No Description available"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            season_description.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            season_description.text = Html.fromHtml(description)
        }

    }

    private fun initRecyclerView(episodes: List<Episode>?) {
        seasonAdapter = SeasonAdapter(this)
        rv_episodes.layoutManager = LinearLayoutManager(context)
        rv_episodes.adapter = seasonAdapter
        if (episodes != null)
            seasonAdapter.setData(episodes)
    }

    private fun initEpisodeView(list: List<Episode>?) {
        val episodes = list?.filter { it.season == position }
        episodes?.sortedBy { it.number }
        initRecyclerView(episodes)
    }

    private fun initViewModel() {
        detailsViewModel = ViewModelProviders.of(parentActivity).get(DetailsViewModel::class.java)
        detailsViewModel.getEpisodes()?.observe(parentActivity, Observer {
            episodeList = it
            initEpisodeView(episodeList)
        })
    }

    override fun onItemClick(episode: Episode) {
        val ep = episodeList?.find { it == episode }
        episode.watched = !episode.watched
        ep?.watched = episode.watched
        detailsViewModel.updateEpisode(episode)
        seasonAdapter.setData(episodeList?.filter { it.season == position }!!)
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: DetailActivity, list: List<Season>, position: Int) =
            SeasonFragment().apply {
                this.parentActivity = parent
                this.seasonList = list
                this.position = position
            }
    }
}