package shock.com.navigation.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.image_item.*
import shock.com.navigation.R
import shock.com.navigation.data.Upload
import java.io.FileNotFoundException
import java.net.URL

class ImageAdapter(private val mContext: Context, var mUpload: ArrayList<Upload>, var listener: ClickListener) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uploadCurrent = mUpload[position]
        //holder.textView.text = uploadCurrent.getName()
        val imagename = uploadCurrent.getImageUrl()
        Glide.with(mContext)
            .load(imagename)
            .placeholder(R.drawable.logo)
            .centerCrop()
            .into(holder.imageItem)

        holder.itemView.setOnClickListener {
            if (imagename != null) {
                listener.onClick(imagename)
            }
        }
    }

    override fun getItemCount(): Int {
        return mUpload.size
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

    interface ClickListener{
        fun onClick(url: String)
    }
}