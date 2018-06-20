package pt.dfsg.seriestracker.ui.detail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import pt.dfsg.seriestracker.data.model.Season

class DetailPagerAdapter(
    private val parent: DetailActivity,
    private val viewModel: DetailsViewModel,
    fragmentManager: FragmentManager,
    private var list : List<Season>
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DetailFragment.newInstance(parent, viewModel)
        } else {
            SeasonFragment.newInstance(parent, viewModel, list, position)
        }
    }

    override fun getCount(): Int {
        return list.count() + 1
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Details"
            else -> "Season $position"
        }
    }
}