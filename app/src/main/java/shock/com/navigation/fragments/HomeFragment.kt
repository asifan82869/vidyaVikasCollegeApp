package shock.com.navigation.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import shock.com.navigation.R
import shock.com.navigation.adapter.SlidAdapter
import shock.com.navigation.view.MainActivity


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(shock.com.navigation.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slidAdapter()
        val nAct: MainActivity = activity as MainActivity
        nAct.supportActionBar?.title = "Home"
        homeText.isVerticalScrollBarEnabled = false
        homeText.loadData(getString(R.string.home_text), "text/html; charset=utf-8", "utf-8")

        library.paintFlags = library.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        nss.paintFlags = nss.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        nss.setOnClickListener{
            nAct.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, NSSFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        library.setOnClickListener{
            nAct.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, LibraryFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }



    }

    private fun slidAdapter(){
        val nAct: MainActivity = activity as MainActivity
        val context = nAct.contaxt
        val adapter = SlidAdapter(context)
        viewpager.adapter = adapter
        indicator.setViewPager(viewpager)


        /*val handler = Handler()
        val timer = Timer()

        var viewPagerId:Int

        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    if(viewpager?.currentItem != null){
                        viewPagerId = viewpager.currentItem
                        if(viewPagerId == 3){
                            viewPagerId = 0
                            viewpager.setCurrentItem(viewPagerId, true)
                        }else{
                            viewPagerId++
                            viewpager.setCurrentItem(viewPagerId, true)
                        }
                    }else{
                        viewPagerId=0
                    }
                }
            }
        }, 4000, 4000)*/
    }


}