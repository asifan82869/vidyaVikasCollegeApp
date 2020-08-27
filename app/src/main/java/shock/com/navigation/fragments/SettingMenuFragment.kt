package shock.com.navigation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting_menu.*
import shock.com.navigation.R
import shock.com.navigation.view.MainActivity

class SettingMenuFragment : Fragment(R.layout.fragment_setting_menu) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nAct: MainActivity = activity as MainActivity
        nAct.supportActionBar?.title = "Setting"

        tvUploadPhotos.setOnClickListener {
            nextFragment("Upload")
        }
        tvDeletePhotos.setOnClickListener {
            nextFragment("Delete")
        }
    }

    private fun nextFragment(data:String){
        val nAct: MainActivity = activity as MainActivity
        nAct.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, OptionSettingFragment(data))
            addToBackStack(null)
            commit()
        }
    }
}