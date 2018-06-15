package pt.dfsg.seriestracker.ui.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_episode.view.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Episode
import pt.dfsg.seriestracker.utils.load


class SeasonAdapter(private var clickCallBack: SeasonAdapter.ClickCallBack) :
    RecyclerView.Adapter<SeasonAdapter.ViewHolder>() {

    private var episodes: List<Episode> = listOf()

    fun setData(episodes: List<Episode>) {
        this.episodes = episodes
        notifyDataSetChanged()
    }

    override fun getItemCount() = episodes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val episode = episodes[position]

        holder.itemView.tv_name.text = episode.name
        holder.itemView.tv_air_date.text = episode.airdate
        holder.itemView.tv_number.text = episode.number.toString()
        holder.itemView.iv_image.load(episode.image?.medium.toString())
        holder.itemView.btn_watched.setImageResource(
            if (episode.watched) R.drawable.ic_visibility_black_24dp
            else R.drawable.ic_visibility_off_black_24dp
        )
        holder.itemView.btn_watched.setOnClickListener { clickCallBack.onItemClick(episode) }
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
        fun onItemClick(episode: Episode)
    }

}