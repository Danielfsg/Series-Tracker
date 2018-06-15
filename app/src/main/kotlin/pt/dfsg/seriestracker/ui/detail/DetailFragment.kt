package pt.dfsg.seriestracker.ui.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_details.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.utils.load

class DetailFragment : Fragment() {

    private lateinit var parentActivity: DetailActivity
    private lateinit var detailsViewModel: DetailsViewModel

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
        detailsViewModel = ViewModelProviders.of(parentActivity).get(DetailsViewModel::class.java)
        initViewsWithData(detailsViewModel.getShow())
    }

    private fun initViewsWithData(show: Show?) {
        when (show) {
            null -> return
            else -> {
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
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: DetailActivity) =
            DetailFragment().apply {
                this.parentActivity = parent
            }
    }
}