package shock.com.navigation.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
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
    }

    private fun slidAdapter(){
        var nAct: MainActivity = activity as MainActivity
        val context = nAct.contaxt
        val adapter = SlidAdapter(context)
        viewpager.adapter = adapter
    }
}