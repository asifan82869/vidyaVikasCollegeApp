package shock.com.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_delete_image.*
import kotlinx.android.synthetic.main.fragment_image_college.progressBar
import kotlinx.android.synthetic.main.fragment_image_college.recyclerViewCollege
import shock.com.navigation.R
import shock.com.navigation.adapter.DeleteimageAdapter
import shock.com.navigation.data.Upload
import shock.com.navigation.view.MainActivity

class DeleteImageFragment(val data: String) : Fragment() {

    var mUpload = ArrayList<Upload>()
    var adapter: DeleteimageAdapter? = null
    val mStoragRef = FirebaseStorage.getInstance().reference
    val mDataBaseRef = FirebaseDatabase.getInstance().reference
    val imageRef = mDataBaseRef.child(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.VISIBLE
        recycle()

        deleteButton.setOnClickListener {
            delete()
        }
    }

    private fun recycle() {

        val nAct: MainActivity = activity as MainActivity

        adapter = DeleteimageAdapter(
            nAct.contaxt,
            mUpload,
            object : DeleteimageAdapter.ICustomAdaptListener {
                override fun onLongClick() {
                    deleteButton.visibility = View.VISIBLE
                }
            })
        recyclerViewDelete.adapter = adapter
        recyclerViewDelete.layoutManager = GridLayoutManager(nAct.contaxt, 2)
        progressBar.visibility = View.GONE

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUpload.clear()
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    var upload: Upload? = postSnapshot.getValue(Upload::class.java)
                    upload?.setKey(postSnapshot.getKey()!!)
                    mUpload.add(upload!!)
                }
                adapter!!.notifyDataSetChanged()

                if (mUpload.size == 0) {
                    errorDatabase.visibility = View.VISIBLE
                    errorDatabase.text = "No Image Found"
                    deleteButton.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(nAct.contaxt, databaseError.message, Toast.LENGTH_LONG).show()
            }
        }

        imageRef.addValueEventListener(postListener)

    }


    private fun delete() {
        var yes = false
        val nAct: MainActivity = activity as MainActivity
        val images = (recyclerViewDelete.adapter as DeleteimageAdapter).isSelectedImage()
        val keys = (recyclerViewDelete.adapter as DeleteimageAdapter).deleteImage()
        if (images.isNotEmpty() && keys.isNotEmpty()){
            for (i in keys.size - 1 downTo 0){
                for (image in images) {
                    val key = keys[i]
                    val url = image.getImageName()
                    val imageref = mStoragRef.child("$data/$url")
                    imageref.delete()
                    val deleteKey = mDataBaseRef.child("$data/$key")
                    deleteKey.removeValue()
                    yes = true
                }
            }
        }else{
            Toast.makeText(nAct, "image and key are empty", Toast.LENGTH_LONG).show()
        }

        if (yes){
            Toast.makeText(nAct, "Image delete Successfully...", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(nAct, "Image delete failed...", Toast.LENGTH_LONG).show()
        }
    }

}