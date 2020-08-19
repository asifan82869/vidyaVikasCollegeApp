package shock.com.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_delete_image.*
import kotlinx.android.synthetic.main.image_item.*
import shock.com.navigation.R
import shock.com.navigation.data.Upload
import java.security.Key

class DeleteimageAdapter(private val mContext: Context, private val mUpload: ArrayList<Upload>, private val listener: ICustomAdaptListener): RecyclerView.Adapter<DeleteimageAdapter.ViewHolder>(){

    var inMode = false
    var context:Context? = null
    var ImageList:ArrayList<Upload>? = null

    init {
        context = mContext
        ImageList = mUpload
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeleteimageAdapter.ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUpload.size
    }

    override fun onBindViewHolder(holder: DeleteimageAdapter.ViewHolder, position: Int) {
        val uploadCurrent = ImageList?.get(position)
        //holder.textView.text = uploadCurrent.getName()
        Glide.with(mContext)
            .load(uploadCurrent?.getImageUrl())
            .placeholder(R.drawable.logo)
            .centerCrop()
            .into(holder.imageItem)

        if (!inMode){
            holder.checkbox.visibility = View.GONE
            uploadCurrent?.isChecked = false
        }else{
            holder.checkbox.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            if (inMode){
                uploadCurrent?.isChecked = !uploadCurrent?.isChecked!!
                holder.checkbox.isChecked = uploadCurrent.isChecked
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!inMode){
                listener.onLongClick()
                toggleSharingMode()
            }
            true
        }

        holder.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            if (uploadCurrent?.isChecked != b){
                uploadCurrent?.isChecked = b
            }
        }
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

    private fun toggleSharingMode(){
        inMode = !inMode
        notifyDataSetChanged()
    }

    fun deleteImage(): ArrayList<String>{
        val keys = ArrayList<String>()
        val selectedImages = isSelectedImage()
        if (selectedImages.isNotEmpty()){
            for (i in mUpload.size-1 downTo 0){
                val key =mUpload[i]
                for (selectedImage in selectedImages){
                    if (key.getKey().equals(selectedImage.getKey())){
                        selectedImage.getKey()?.let { keys.add(it) }
                    }
                }

            }
        }
        return keys
    }

    fun isSelectedImage():ArrayList<Upload>{
        val selectedImage = ArrayList<Upload>()
        for(upload in mUpload){
            if (upload.isChecked){
                selectedImage.add(upload)
            }
        }
        return selectedImage
    }

    interface ICustomAdaptListener{
        fun onLongClick()
    }

}
