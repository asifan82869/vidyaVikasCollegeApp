package shock.com.navigation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_home.*
import shock.com.navigation.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutCont.isVerticalScrollBarEnabled = false
        aboutCont.loadData(getString(R.string.about_text), "text/html; charset=utf-8", "utf-8")
    }

}