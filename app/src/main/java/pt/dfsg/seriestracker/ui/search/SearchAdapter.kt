package pt.dfsg.seriestracker.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import pt.dfsg.seriestracker.R
import pt.dfsg.seriestracker.data.model.Model


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var search: List<Model.Search> = listOf()

    fun setData(search: List<Model.Search>) {
        this.search = search
        notifyDataSetChanged()
    }

    override fun getItemCount() = search.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.textView.text = search[position].show?.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}