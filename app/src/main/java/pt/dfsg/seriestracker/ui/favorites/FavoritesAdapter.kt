package pt.dfsg.seriestracker.ui.favorites

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Show


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

        holder.itemView.textView.text =
                "${show.name} \n" +
                "${show.status} \n" +
                "${show.premiered} \n" +
                "${show.network?.name ?: show.webChannel?.name}"

        holder.itemView.setOnClickListener { clickCallBack.onItemClick(show) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ClickCallBack {
        fun onItemClick(show: Show)
    }

}