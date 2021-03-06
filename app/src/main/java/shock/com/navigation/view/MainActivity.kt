package shock.com.navigation.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
    var toolbar: Toolbar? = null
    var count = 0
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
                    replaceFragment(home)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_gallery -> {
                    replaceFragment(gallery)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_about -> {
                    replaceFragment(about)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_contact -> {
                    replaceFragment(contact)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_other -> {
                    replaceFragment(OtherFragment())
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menu_setting -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    replaceFragment(setting)
                }
                R.id.share_app -> {
                    val sharingIntent: Intent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra( Intent.EXTRA_TEXT, "http://gg.gg/VidyaVikasApp")
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

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLayout, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun settingLog(){
        if(user == null){
            val builder = AlertDialog.Builder(this)
            val logInDialog = LayoutInflater.from(this).inflate(R.layout.login, null)
            builder.setView(logInDialog).setTitle("LOGIN")
            val mAlert = builder.show()
            mAlert.login.setOnClickListener {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Please wait for few second")
                progressDialog.show()
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
                            progressDialog.dismiss()
                        }else{
                            try {
                                throw task.exception!!
                            }catch (e: FirebaseAuthInvalidCredentialsException){
                                progressDialog.dismiss()
                                Toast.makeText(this, "please enter a correct userName and Password",
                                    Toast.LENGTH_LONG).show()
                            }catch (e: FirebaseNetworkException){
                                progressDialog.dismiss()
                                Toast.makeText(this, "check your internet connection",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this, "please enter userName and Password", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            hideSettingItem!!.isVisible = true
            hideLogoutItem!!.isVisible = true
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.backStackEntryCount

        if (fragment == 0) {
            if (count == 1 ){
                super.onBackPressed()

            }else{
                count += 1
                Toast.makeText(this,"Press again for exist", Toast.LENGTH_SHORT).show()
            }
        } else {
            supportFragmentManager.popBackStack()
        }
        Handler().postDelayed(Runnable {
            count = 0
        },5000)
    }
}