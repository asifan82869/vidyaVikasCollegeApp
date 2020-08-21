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
        tvUploadPhotos.setOnClickListener {
            nextFragment("Upload")
        }
        tvDeletePhotos.setOnClickListener {
            nextFragment("Delete")
        }
    }

    private fun nextFragment(data:String){
        var nAct: MainActivity = activity as MainActivity
        nAct.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, OptionSettingFragment(data))
            nAct.addToBackPress += 1
            addToBackStack(null)
            commit()
        }
    }
}