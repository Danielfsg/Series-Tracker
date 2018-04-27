package pt.dfsg.seriestracker.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import pt.dfsg.seriestracker.ui.favorites.FavoritesFragment
import pt.dfsg.seriestracker.ui.search.SearchFragment

class SectionsPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FavoritesFragment.newInstance()
            1 -> return SearchFragment.newInstance()
        }
        return FavoritesFragment.newInstance()
    }

    override fun getCount(): Int {
        return 2
    }
}