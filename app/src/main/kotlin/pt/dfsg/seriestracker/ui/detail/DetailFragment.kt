package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_details.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.data.model.Season
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.utils.load
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var parentActivity: DetailActivity
    private lateinit var detailsViewModel: DetailsViewModel

    private var episodeList: List<Episode>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
    }

    private fun initViewModel() {
        //detailsViewModel = ViewModelProviders.of(parentActivity).get(DetailsViewModel::class.java)
        initViewsWithData(detailsViewModel.getShow())
    }


    private fun initViewsWithData(show: Show?) {
        when (show) {
            null -> return
            show -> {
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

                if (show.status.equals("Ended")) {
                    next_episodes_view.visibility = View.GONE
                } else {
                    next_episodes_view.visibility = View.VISIBLE
                    if (episodeList == null) {
                        detailsViewModel.getEpisodes()?.observe(parentActivity, Observer {
                            episodeList = it
                            initNextEpisode(episodeList)
                        })
                    } else {
                        initNextEpisode(episodeList)
                    }
                }
            }
        }
    }

    private fun initNextEpisode(episodeList: List<Episode>?) {
        when (episodeList) {
            null -> return
            episodeList -> {
                var count = 0
                var text = ""
                episodeList.sortedBy { selector(it) }
                episodeList.forEach {
                    val stringDate = it.airdate
                    if (stringDate != null && !stringDate.isBlank()) {
                        val date = convertToMilliseconds(stringDate)
                        if (isStringHigherThanToday(date)) {
                            if (count > 0) text += "\n"
                            val temp = formatDate(stringDate)
                            text += "$temp - ${it.airtime} | #${it.number} | ${it.name}\n"
                            next_episodes_info.text = text
                            count++
                        }
                    }
                }
                if (count == 0)
                    if (next_episodes_view != null)
                        next_episodes_view.visibility = View.GONE
            }
        }
    }

    private fun convertToMilliseconds(stringDate: String): Long =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(stringDate).time

    private fun isStringHigherThanToday(date: Long): Boolean =
        Calendar.getInstance().timeInMillis <= date

    private fun formatDate(stringDate: String): String =
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(stringDate)
        )

    private fun selector(p: Episode): Int? = p.number

    companion object {
        @JvmStatic
        fun newInstance(parent: DetailActivity, viewModel: DetailsViewModel) =
            DetailFragment().apply {
                this.parentActivity = parent
                this.detailsViewModel = viewModel
            }
    }
}