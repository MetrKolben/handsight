package com.jindrak.handsight

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.navigation.NavigationView


lateinit var size: Size

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity()
{

    private lateinit var hamburgerButton: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fragment: FragmentContainerView
    private val codePermissionCamera = 101

    @SuppressLint("MissingInflatedId", "ResourceType", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val fontSizeSeek = findViewById<SeekBar>(R.id.seekBar)
        val switchFormation = findViewById<Switch>(R.id.switchView)

        getCameraPermission()

        fragment = findViewById(R.id.fragment)
        navigationView.getHeaderView(0).clipToOutline = true

        fontSizeSeek.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Utils.font_size = progress
            }
        })

        switchFormation.setOnCheckedChangeListener { _, isChecked ->
            Utils.word_formation = isChecked
            RecognitionFragment.switch.switch(isChecked)
            if (isChecked) {
                RecognitionFragment.timer.pause()
            } else {
                val currentFragment = supportFragmentManager.findFragmentById(fragment.id)
                if (currentFragment!!.javaClass == RecognitionFragment.Companion::class.java.declaringClass) {
                    RecognitionFragment.timer.start()
                }
            }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        size = screenSize()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hamburgerButton = findViewById(R.id.hamburgerButton)
        hamburgerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }

    private fun getCameraPermission () {
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.CAMERA),
                codePermissionCamera)
        }
    }

    private fun screenSize(): Size {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return Size(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.dictionary -> DictionaryFragment()
            R.id.recognition -> RecognitionFragment()
            else -> {null}
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment!!)
            addToBackStack(null)
            commit()
        }
        return true
    }
}