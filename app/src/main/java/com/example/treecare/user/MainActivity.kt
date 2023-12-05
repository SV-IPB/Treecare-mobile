package com.example.treecare.user

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.treecare.LoginActivity
import com.example.treecare.R
import com.example.treecare.user.camera.CameraActivity
import com.example.treecare.user.identitas_pohon.TambahIdentitasPohonActivity
import com.example.treecare.user.kerusakan_pohon.DetailKerusakanPohonActivity
import com.example.treecare.user.kerusakan_pohon.KerusakanPohonActivity
import com.example.treecare.user.kerusakan_pohon.TambahKerusakanPohonActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import org.osmdroid.config.Configuration

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Configuration.getInstance().userAgentValue = "TreeCare/1.0 (Android; osmdroid)"

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val ivSidebar: ImageView = findViewById(R.id.ivSidebar)

        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 100f)
            .build()
        bottomAppBar.elevation = 0f

        bottomNavView = findViewById(R.id.bottomNavView)
        bottomNavView.background = null
        bottomNavView.menu.getItem(1).isEnabled = false
        bottomNavView.menu.getItem(2).isEnabled = false
        bottomNavView.menu.getItem(3).isEnabled = false
        bottomNavView.elevation = 0f

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setImageResource(R.drawable.scan_fab)
        fab.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flContainer, HomeFragment())
        fragmentTransaction.commit()

        fab.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.mihome -> {
                    fragmentTransaction.replace(R.id.flContainer, HomeFragment())
                    fragmentTransaction.commit()
                    ivSidebar.visibility = View.VISIBLE
                    true
                }
                R.id.mihistory -> {
                    fragmentTransaction.replace(R.id.flContainer, HistoryFragment())
                    fragmentTransaction.commit()
                    ivSidebar.visibility = View.INVISIBLE
                    true
                }
                else -> false
            }
        }

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            val fragmentManagerBar: FragmentManager = supportFragmentManager
            val fragmentTransactionBar: FragmentTransaction = fragmentManagerBar.beginTransaction()

            when(it.itemId) {
                R.id.miprofile -> {
                    fragmentTransactionBar.replace(R.id.flContainer, ProfileFragment())
                    fragmentTransactionBar.commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    ivSidebar.visibility = View.INVISIBLE
                    true
                }
                R.id.milogout -> {
                    showLogoutDialog()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    fun onSidebarClicked(view: View) {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun showLogoutDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_logout)
        val btnLogoutDialog: AppCompatButton = dialog.findViewById(R.id.btnLogoutDialog)
        dialog.setTitle("Logout")
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        btnLogoutDialog.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}