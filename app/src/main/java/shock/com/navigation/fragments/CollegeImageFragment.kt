package shock.com.navigation.fragments

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_image_college.*
import kotlinx.android.synthetic.main.show_image.*
import shock.com.navigation.R
import shock.com.navigation.adapter.ImageAdapter
import shock.com.navigation.data.Upload
import shock.com.navigation.view.MainActivity
import java.net.URL

class CollegeImageFragment: Fragment() {

    var mUpload = ArrayList<Upload>()
    var adapter:ImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_college, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.VISIBLE
        recycle()
    }

    private fun recycle(){
        val nAct: MainActivity = activity as MainActivity
        val mDataBaseRef = FirebaseDatabase.getInstance().reference
        val imageRef = mDataBaseRef.child("CollegeImage")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUpload.clear()
                for (postSnapshot: DataSnapshot in dataSnapshot.children){
                    var upload: Upload? = postSnapshot.getValue(Upload::class.java)
                    mUpload.add(upload!!)
                }

                if (mUpload.size != 0) {
                    adapter = ImageAdapter(nAct.contaxt, mUpload, object: ImageAdapter.ClickListener{
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
                    recyclerViewCollege.adapter = adapter
                    recyclerViewCollege.layoutManager = GridLayoutManager(nAct.contaxt, 2)
                    (recyclerViewCollege.adapter as ImageAdapter).notifyDataSetChanged()
                }else{
                    errorDatabase.visibility = View.VISIBLE
                    errorDatabase.text = "No Image Found"
                }

                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(nAct.contaxt, databaseError.message, Toast.LENGTH_LONG).show()
            }
        }
        imageRef.addValueEventListener(postListener)
    }

}