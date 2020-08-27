package shock.com.navigation.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_image_college.*
import kotlinx.android.synthetic.main.fragment_nss.*
import kotlinx.android.synthetic.main.show_image.*
import shock.com.navigation.R
import shock.com.navigation.adapter.HorizontalImageAdapter
import shock.com.navigation.data.Upload
import shock.com.navigation.view.MainActivity

class NSSFragment : Fragment() {

    var mUpload = ArrayList<Upload>()
    var adapter: HorizontalImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nss, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nAct: MainActivity = activity as MainActivity
        nAct.supportActionBar?.title = "NSS"

        nssText.isVerticalScrollBarEnabled = false
        nssText.loadData(getString(R.string.nss_text), "text/html; charset=utf-8", "utf-8")

        recycle()

        val instaUrl = "https://www.instagram.com/nss_unit_2019_20/"
        val facebookUrl = "https://www.facebook.com/VikasCollegeNSSUnit/"

        insta.setOnClickListener {
            socialWeb(instaUrl)
        }
        facebook.setOnClickListener {
            socialWeb(facebookUrl)
        }

    }

    private fun socialWeb(url: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun recycle(){
        val nAct: MainActivity = activity as MainActivity
        val mDataBaseRef = FirebaseDatabase.getInstance().reference
        val imageRef = mDataBaseRef.child("NSSImage")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUpload.clear()
                adapter = HorizontalImageAdapter(nAct.contaxt, mUpload, object: HorizontalImageAdapter.ClickListener{
                    override fun onClick(url: String) {
                        val builder = Dialog(nAct)
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        builder.setContentView(layoutInflater.inflate(R.layout.show_image, null))
                        Glide.with(nAct)
                            .load(url)
                            .placeholder(R.drawable.logo)
                            .into(builder.showImage)
                        builder.show()
                    }
                })
                nssRecycler.adapter = adapter
                val layoutManager = LinearLayoutManager(nAct.contaxt, LinearLayoutManager.HORIZONTAL, false)
                nssRecycler.layoutManager = layoutManager
//                nssRecycler.itemAnimator = DefaultItemAnimator()

                for (postSnapshot: DataSnapshot in dataSnapshot.children){
                    val upload: Upload? = postSnapshot.getValue(Upload::class.java)
                    mUpload.add(upload!!)
                }

                (nssRecycler.adapter as HorizontalImageAdapter).notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(nAct.contaxt, databaseError.message, Toast.LENGTH_LONG).show()
            }
        }
        imageRef.addValueEventListener(postListener)
    }
}