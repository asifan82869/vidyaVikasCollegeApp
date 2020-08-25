package shock.com.navigation.fragments

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
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

    private val REQUEST_EXTERNAL_STORAGE = 100
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
            permission()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun permission(){
        val nAct: MainActivity = activity as MainActivity
        if (ActivityCompat.checkSelfPermission(nAct, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(nAct,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
            chooseImage()
        } else {
            chooseImage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val nAct: MainActivity = activity as MainActivity
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(nAct,"Permission success", Toast.LENGTH_SHORT).show()
                    chooseImage()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(nAct,"Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
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
        val nAct: MainActivity = activity as MainActivity
        val cR: ContentResolver = nAct.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }

    private fun uploadImageFile(){
        val nAct: MainActivity = activity as MainActivity
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun chooseImage(){
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "image/*"
        val mimeType = arrayOf("image/jpeg","image/jpg","image/png")
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
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
