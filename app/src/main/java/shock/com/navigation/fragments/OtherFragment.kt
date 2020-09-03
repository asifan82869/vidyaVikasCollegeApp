package shock.com.navigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_other.*
import shock.com.navigation.R
import shock.com.navigation.view.MainActivity

class OtherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nAct: MainActivity = activity as MainActivity
        cardOne.setOnClickListener {
            nAct.replaceFragment(LibraryFragment())
        }
        cardTwo.setOnClickListener {
            nAct.replaceFragment(NSSFragment())
        }
    }
}