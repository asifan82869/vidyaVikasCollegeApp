package shock.com.navigation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_gallery.*
import shock.com.navigation.R
import shock.com.navigation.adapter.TabAdapter
import shock.com.navigation.view.MainActivity

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nAct: MainActivity = activity as MainActivity
        val viewPager = nAct.findViewById<ViewPager>(R.id.tabLayoutViewPager) as ViewPager
        setUpViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    fun setUpViewPager(viewpager: ViewPager){

        var nAct: MainActivity = activity as MainActivity
        val tabAdapter = TabAdapter(nAct.supportFragmentManager)
        tabAdapter.addFragment(CollegeImageFragment(), "COLLEGE")
        tabAdapter.addFragment(EventImageFragment(), "EVENT")
        tabAdapter.addFragment(SportImageFragment(), "SPORT")
        tabAdapter.addFragment(LibraryImageFragment(),"LIBRARY")
        viewpager.adapter = tabAdapter
    }

}