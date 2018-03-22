package layout.com.anew.layout1

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_new_word -> {
                Toast.makeText(this,"call 生词本 activity",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_finished_word-> {
                Toast.makeText(this,"call 已完成单词 activity",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_coming_word -> {
                Toast.makeText(this,"call 未背单词 activity",Toast.LENGTH_SHORT).show()
            }
            R.id.nav_setting -> {
                Toast.makeText(this,"call 设置 activity",Toast.LENGTH_SHORT).show()
                val changeToSetting = Intent()
                changeToSetting.setClass(this,SettingsActivity::class.java)
                startActivity(changeToSetting)
            }
            R.id.nav_about -> {
                Toast.makeText(this,"call 关于 activity",Toast.LENGTH_SHORT).show()
            }

        }
        profile_picture.setOnClickListener(){
            Toast.makeText(this,"call 个人资料 activity",Toast.LENGTH_SHORT).show()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
