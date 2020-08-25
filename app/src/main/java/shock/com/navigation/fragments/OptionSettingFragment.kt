package shock.com.navigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_option_setting.*
import shock.com.navigation.R
import shock.com.navigation.view.MainActivity

class OptionSettingFragment(private val option: String) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collegeUploadPhotos.setOnClickListener {
            fragmentReplace("CollegeImage")
        }
        eventUploadPhotos.setOnClickListener {
            fragmentReplace("EventImage")
        }
        sportUploadPhotos.setOnClickListener {
            fragmentReplace("SportImage")
        }
        libraryUploadPhotos.setOnClickListener {
            fragmentReplace("LibraryImage")
        }
        nssUploadPhotos.setOnClickListener {
            fragmentReplace("NSSImage")
        }
    }

    private fun fragmentReplace(data:String){
        val nAct: MainActivity = activity as MainActivity
        if (option == "Upload"){
            nAct.supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentLayout, UploadImageFragment(data))
                addToBackStack(null)
                commit()
            }
            nAct.addToBackPress += 1
        }
        if (option == "Delete"){
            nAct.supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentLayout, DeleteImageFragment(data))
                addToBackStack(null)
                commit()
            }
            nAct.addToBackPress += 1
        }

    }

}