package pt.dfsg.seriestracker.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import kotlinx.android.synthetic.main.activity_detail.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val show = intent.extras.get("SHOW") as Show


        show_name.text = show.name
        show_premiered.text = show.premiered
        show_channel.text = show.network?.name ?: show.webChannel?.name
        show_runtime.text = show.runtime.toString()
        show_status.text = show.status
        show_rating.text = show.rating?.average.toString()
        show_summary.text = Html.fromHtml(show.summary, Html.FROM_HTML_MODE_COMPACT)


    }
}
