package shock.com.navigation.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.login.*
import shock.com.navigation.R
import shock.com.navigation.fragments.*

class MainActivity : AppCompatActivity() {
    val contaxt = this
    var addToBackPress = 0
    var toolbar: Toolbar? = null
    var mAuth = FirebaseAuth.getInstance()
    private var user = mAuth.currentUser
    private var hideSettingItem:MenuItem? = null
    private var hideLogoutItem:MenuItem? = null
    private var menu:Menu? = null

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(user == null){
            menuInflater.inflate(R.menu.toolbar_menu, menu)
        }
        this.menu = menu
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.login ->{
                settingLog()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun start(){
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentLayout, home)
            commit()
        }

        val drawerLayout: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer)
        val nav: NavigationView = findViewById<NavigationView>(R.id.navmenu)

        val menuItem = nav.menu
        hideSettingItem = menuItem.findItem((R.id.menu_setting))
        hideLogoutItem = menuItem.findItem(R.id.menu_logout)
        hideSettingItem!!.isVisible = user != null
        hideLogoutItem!!.isVisible = user != null

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
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_gallery -> {
                    supportActionBar?.title = "Gallery"
                    replaceFragment(gallery)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_about -> {
                    supportActionBar?.title = "About Us"
                    replaceFragment(about)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_contact -> {
                    supportActionBar?.title = "Contact Us"
                    replaceFragment(contact)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_setting -> {
                    supportActionBar?.title = "Setting"
                    drawerLayout.closeDrawer(GravityCompat.START)
                    replaceFragment(setting)
                    addToBackPress += 1
                }
                R.id.share_app -> {
                    val sharingIntent: Intent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra( Intent.EXTRA_TEXT, "http://gg.gg/vikasAap")
                    startActivity(sharingIntent)
                }
                R.id.menu_logout ->{
                    val build = AlertDialog.Builder(this)
                    build.setTitle("LogOut")
                    build.setMessage("Are you sure you want to LogOut?")
                    build.setNegativeButton("No"){dialogInterface, which ->
                        dialogInterface.cancel()
                    }
                    build.setPositiveButton("Yes"){dialogInterface, which ->
                        mAuth.signOut()
                        toolbar?.menu?.clear()
                        menuInflater.inflate(R.menu.toolbar_menu, menu)
                        hideSettingItem!!.isVisible = false
                        hideLogoutItem!!.isVisible = false
                        user = null
                        supportActionBar?.title = "Home"
                        replaceFragment(home)
                    }
                    val alert = build.create()
                    alert.show()
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, fragment)
            commit()
        }
    }

    private fun settingLog(){
        if(user == null){
            val builder = AlertDialog.Builder(this)
            val logInDialog = LayoutInflater.from(this).inflate(R.layout.login, null)
            builder.setView(logInDialog).setTitle("LOGIN")
            val mAlert = builder.show()
            mAlert.login.setOnClickListener {
                val email = mAlert.userName.text.toString()
                val pass = mAlert.password.text.toString()
                if(email.isNotEmpty() && pass.isNotEmpty()){
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            mAlert.dismiss()
                            toolbar?.menu?.clear()
                            hideSettingItem!!.isVisible = true
                            hideLogoutItem!!.isVisible = true
                            user = mAuth.currentUser
                        }else{
                            try {
                                throw task.exception!!
                            }catch (e: FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(this, "please enter a correct userName and Password",
                                    Toast.LENGTH_LONG).show()
                            }catch (e: FirebaseNetworkException){
                                Toast.makeText(this, "check your internet connection",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(this, "please enter userName and Password", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            hideSettingItem!!.isVisible = true
            hideLogoutItem!!.isVisible = true
        }
    }

    override fun onBackPressed() {
       if(addToBackPress == 1 || addToBackPress == 0){
           val build = AlertDialog.Builder(this)
           build.setTitle("Really Exit?")
           build.setMessage("Are you sure you want to exit?")
           build.setNegativeButton("No"){dialogInterface, which ->
               dialogInterface.cancel()
           }
           build.setPositiveButton("Yes"){dialogInterface, which ->
               super.onBackPressed()
           }
           val alert = build.create()
           alert.show()
       }else{
           addToBackPress -= 1
           super.onBackPressed()
       }
    }
}