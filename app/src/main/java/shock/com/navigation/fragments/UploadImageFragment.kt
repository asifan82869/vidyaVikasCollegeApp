package shock.com.navigation.fragments

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_upload_image.*
import shock.com.navigation.R
import shock.com.navigation.data.Upload
import shock.com.navigation.view.MainActivity

class UploadImageFragment(private val data: String) : Fragment() {


    var refStorage = FirebaseStorage.getInstance().getReference(data)
    private var imageUri:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_image, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnChooseImage.setOnClickListener {
            chooseImage()
        }
        btnUploadImage.setOnClickListener {
            val nAct: MainActivity = activity as MainActivity
            if (verifyAvailableNetwork()){
                progressbar.visibility = View.VISIBLE
                uploadImageFile()
            }else{
                Toast.makeText(nAct.contaxt,"on internet connection",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyAvailableNetwork():Boolean{
        val nAct: MainActivity = activity as MainActivity
        val connectivityManager=nAct.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    private fun getFileExtension(uri: Uri?): String? {
        var nAct: MainActivity = activity as MainActivity
        val cR: ContentResolver = nAct.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }

    private fun uploadImageFile(){
        var nAct: MainActivity = activity as MainActivity
        val mDatabase = FirebaseDatabase.getInstance().getReference(data);
        if (imageUri != null){

            val imageId = data + System.currentTimeMillis() + "." + getFileExtension(imageUri)
            val ref = refStorage.child(imageId);
            ref.putFile(imageUri!!)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    Toast.makeText(nAct.contaxt,"Image Uploaded successfully", Toast.LENGTH_LONG).show()

                    UploadImage.visibility = View.GONE
                    btnUploadImage.visibility = View.GONE
                    progressbar.visibility = View.GONE
                    val imagePath = taskSnapshot.downloadUrl.toString()
                    val upload = Upload("Image", imageId, imagePath)
                    val uploadId: String? = mDatabase.push().key
                    mDatabase.child(uploadId!!).setValue(upload)

                })
                .addOnFailureListener(OnFailureListener {
                    Toast.makeText(nAct.contaxt,"Image Uploaded Failed...", Toast.LENGTH_LONG).show()
                })
        }
    }

    private fun chooseImage(){
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "image/*"
        startActivityForResult(i,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val nAct: MainActivity = activity as MainActivity
        if (requestCode == 100 && resultCode == -1 && data?.data != null) {
            imageUri = data.data
            val inputStream = imageUri?.let { nAct.contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            UploadImage.setImageBitmap(bitmap)
            UploadImage.visibility = View.VISIBLE
            btnUploadImage.visibility = View.VISIBLE
        }
    }
}
