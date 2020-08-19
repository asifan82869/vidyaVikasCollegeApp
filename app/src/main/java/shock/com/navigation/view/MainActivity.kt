package shock.com.navigation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import shock.com.navigation.R
import shock.com.navigation.fragments.*


class MainActivity : AppCompatActivity() {
    val contaxt = this
    var backPress: Long = 0
    var addToBackPress = 0
    var toolbar: Toolbar? = null
    private val home = HomeFragment()
    private val about = AboutFragment()
    private val contact = ContactFragment()
    private val gallery = GalleryFragment()
    private val setting = SettingMenuFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"

        start()

    }


    fun start(){
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentLayout, home)
            commit()
        }

        val drawerLayout: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer)
        val nav: NavigationView = findViewById<NavigationView>(R.id.navmenu)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    supportActionBar?.title = "Home"
                    replaceFragment(home)
                    addToBackPress += 1
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_gallery -> {
                    supportActionBar?.title = "Gallery"
                    replaceFragment(gallery)
                    addToBackPress += 1
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_about -> {
                    supportActionBar?.title = "About Us"
                    replaceFragment(about)
                    addToBackPress += 1
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_contact -> {
                    supportActionBar?.title = "Contact Us"
                    replaceFragment(contact)
                    addToBackPress += 1
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_setting -> {
                    supportActionBar?.title = "Setting"
                    replaceFragment(setting)
                    addToBackPress += 1
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.share_app -> {
                    val sharingIntent: Intent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra( Intent.EXTRA_TEXT, "https://drive.google.com/file/d/1aEDdqZpCxVi_6nVqyQWgrSuuZgt_6gIV/view?usp=sharing")
                    startActivity(sharingIntent)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onBackPressed() {
        if (addToBackPress == 0){
            val t = System.currentTimeMillis()
            if (t - backPress > 2000) {
                backPress = t;
                Toast.makeText(this, "Press back again to exit",
                    Toast.LENGTH_SHORT).show();
            }else{
                super.onBackPressed()
            }
        }else{
            addToBackPress -= 1
            super.onBackPressed()
        }
    }
}