package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.Observer
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seasonAdapter = SeasonAdapter(parentActivity,this)
        rv_episodes.layoutManager = LinearLayoutManager(context)
        rv_episodes.adapter = seasonAdapter
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
        initSeasonInfo()
    }

    private fun initSeasonInfo() {
        season_info_view.visibility = View.VISIBLE
        season_name.visibility = View.VISIBLE
        season_description.visibility = View.VISIBLE

        val season = seasonList?.get(position - 1)
        val seasonName = season?.name ?: "TBD"
        season_name.text = seasonName
        if (season?.name.isNullOrBlank()) season_name.visibility = View.GONE

        var description = season?.summary
        if (description == null || description.isEmpty() || description.isBlank())
            description = "No Description available"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            season_description.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            season_description.text = Html.fromHtml(description)
        }

    }

    private fun updateRecyclerView(episodes: List<Episode>?) {
        if (episodes != null)
            seasonAdapter.setData(episodes)
    }

    private fun selector(p: Episode): Int? = p.number

    private fun filterList(list: List<Episode>?): List<Episode>? =
        list?.filter { it.season == position }?.sortedBy { selector(it) }


    private fun initViewModel() {
        if (episodeList == null) {
            detailsViewModel.getEpisodes()?.observe(parentActivity, Observer {
                episodeList = it
                updateRecyclerView(filterList(episodeList))
            })
        } else {
            updateRecyclerView(filterList(episodeList))
        }
    }

    override fun onWatchedClick(episode: Episode, position: Int) {
        val ep = episodeList?.find { it == episode }
        episode.watched = !episode.watched
        ep?.watched = episode.watched
        detailsViewModel.updateEpisode(episode)
        filterList(episodeList)?.let { seasonAdapter.updateItem(position, it) }
    }

    override fun onItemClick(episode: Episode, position: Int) {
        episode.isOpen = !episode.isOpen
        seasonAdapter.notifyItemChanged(position)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            parent: DetailActivity,
            viewModel: DetailsViewModel,
            list: List<Season>,
            position: Int
        ) = SeasonFragment().apply {
            this.parentActivity = parent
            this.detailsViewModel = viewModel
            this.seasonList = list
            this.position = position
        }
    }
}