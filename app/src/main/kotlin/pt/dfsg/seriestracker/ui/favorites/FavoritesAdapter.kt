package pt.dfsg.seriestracker.ui.favorites

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_show.view.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show
import pt.dfsg.seriestracker.utils.load


class FavoritesAdapter(private var clickCallBack: FavoritesAdapter.ClickCallBack) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var favoritesList: List<Show> = listOf()

    fun setData(list: List<Show>) {
        this.favoritesList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = favoritesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val show = favoritesList[position]

        holder.itemView.tv_name.text = show.name
        holder.itemView.tv_status.text = show.status
        holder.itemView.tv_genres.text = show.genres?.joinToString(", ")
        holder.itemView.tv_other.text = show.network?.name ?: show.webChannel?.name
        holder.itemView.setOnClickListener { clickCallBack.onItemClick(show) }

        holder.itemView.image_view.load(show.image?.medium.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_show, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ClickCallBack {
        fun onItemClick(show: Show)
    }

}