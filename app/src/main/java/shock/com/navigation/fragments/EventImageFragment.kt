package shock.com.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_image_college.errorDatabase
import kotlinx.android.synthetic.main.fragment_image_college.progressBar
import kotlinx.android.synthetic.main.fragment_image_event.*
import shock.com.navigation.R
import shock.com.navigation.adapter.ImageAdapter
import shock.com.navigation.data.Upload
import shock.com.navigation.view.MainActivity

class EventImageFragment: Fragment() {
    var mUpload = ArrayList<Upload>()
    var adapter: ImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.VISIBLE
        recycle()
    }

    private fun recycle(){
        val nAct: MainActivity = activity as MainActivity
        val mDataBaseRef = FirebaseDatabase.getInstance().reference
        val imageRef = mDataBaseRef.child("EventImage")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUpload.clear()
                for (postSnapshot: DataSnapshot in dataSnapshot.children){
                    var upload: Upload? = postSnapshot.getValue(Upload::class.java)
                    mUpload.add(upload!!)
                }

                if (mUpload.size != 0) {
                    adapter = ImageAdapter(nAct.contaxt, mUpload)
                    recyclerViewEvent.adapter = adapter
                    recyclerViewEvent.layoutManager = GridLayoutManager(nAct.contaxt, 2)
                    (recyclerViewEvent.adapter as ImageAdapter).notifyDataSetChanged()
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