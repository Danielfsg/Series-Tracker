package pt.dfsg.seriestracker.ui.detail

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_episode.view.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.utils.load


class SeasonAdapter(private val context: Context, private var clickCallBack: SeasonAdapter.ClickCallBack) :
    RecyclerView.Adapter<SeasonAdapter.ViewHolder>() {

    private var episodes: List<Episode> = listOf()

    fun setData(episodes: List<Episode>) {
        this.episodes = episodes
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, episodes: List<Episode>) {
        this.episodes = episodes
        notifyItemChanged(position)
    }

    override fun getItemCount() = episodes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val episode = episodes[position]

        holder.itemView.tv_name.text = episode.name
        holder.itemView.tv_air_date.text = episode.airdate
        holder.itemView.tv_number.text = episode.number.toString()
//        if (episode.image?.medium.isNullOrEmpty())
//            holder.itemView.iv_image.visibility = View.GONE
//         else
//            holder.itemView.iv_image.visibility = View.VISIBLE
            holder.itemView.iv_image.load(episode.image?.medium.toString())


        if (episode.watched) {
            holder.itemView.view_mask.visibility = View.VISIBLE
            holder.itemView.tv_watched.text = context.getString(R.string.watched)
            holder.itemView.tv_watched.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_visibility_black_24dp,
                0,
                0
            )
        } else {
            holder.itemView.view_mask.visibility = View.GONE
            holder.itemView.tv_watched.text = context.getString(R.string.unwatched)
            holder.itemView.tv_watched.setCompoundDrawablesWithIntrinsicBounds(
                0,
                R.drawable.ic_visibility_off_black_24dp,
                0,
                0
            )
        }

        holder.itemView.tv_watched.setOnClickListener {
            clickCallBack.onWatchedClick(
                episode,
                position
            )
        }

        val description = episode.summary ?: "No Description available"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.itemView.tv_description.text =
                    Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.itemView.tv_description.text = Html.fromHtml(description)
        }

        if (episode.isOpen) holder.itemView.description_view.visibility = View.VISIBLE
        else holder.itemView.description_view.visibility = View.GONE

        holder.itemView.item_layout.setOnClickListener {
            clickCallBack.onItemClick(
                episode,
                position
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_episode,
                parent,
                false
            )
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ClickCallBack {
        fun onWatchedClick(episode: Episode, position: Int)
        fun onItemClick(episode: Episode, position: Int)
    }


}