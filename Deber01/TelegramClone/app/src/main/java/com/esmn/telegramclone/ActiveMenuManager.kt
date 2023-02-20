package com.esmn.telegramclone

import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ActiveMenuManager(
    val activity: AppCompatActivity
) {
    private lateinit var toggle: ActionBarDrawerToggle

    fun activeMenu() : ActionBarDrawerToggle{
        val drawerLayout: DrawerLayout = activity.findViewById(R.id.drawerLayout)
        val navView: NavigationView = activity.findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(activity, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_chats -> goToActivity(MainActivity::class.java)
            }
            true
        }
        return toggle
    }

    private fun goToActivity(activityClass: Class<MainActivity>) {
        activity.startActivity(Intent(activity, activityClass))
    }
}