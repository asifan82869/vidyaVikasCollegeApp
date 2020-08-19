package shock.com.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import shock.com.navigation.R

class SlidAdapter(val context: Context): PagerAdapter() {
    private var layoutinflater: LayoutInflater? = null

    private val images = arrayOf(
        R.drawable.c_1,
        R.drawable.c_2,
        R.drawable.c_3,
        R.drawable.c_4
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object`)
    }

    override fun getCount(): Int {
        return  images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutinflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutinflater!!.inflate(R.layout.slid_image, null)
        val image = v.findViewById<View>(R.id.slid_imageview) as ImageView
        image.setImageResource(images[position])

        val vp = container as ViewPager
        vp.addView(v, 0)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }
}