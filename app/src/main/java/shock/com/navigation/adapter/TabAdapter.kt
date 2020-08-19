package shock.com.navigation.adapter

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val fragmentList = ArrayList<Fragment>()
    val fragmentTitle = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String){

        fragmentList.add(fragment)
        fragmentTitle.add(title)
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle.get(position)
    }
}