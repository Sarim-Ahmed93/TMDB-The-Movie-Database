package com.example.tmdb_themoviedatabase.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tmdb_themoviedatabase.R
import com.example.tmdb_themoviedatabase.main.backend.AppModel

class MainActivity : AppCompatActivity() {
    private var _optionsMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun setupAppBar(title: String = "", subTitle: String = "") {
        /*_optionsMenu?.findItem(R.id.action_menu_refresh)?.apply {
            isVisible = AppModel.refreshAccessible.value ?: false
        }
        _optionsMenu?.findItem(R.id.action_menu_settings)?.apply {
            isVisible = AppModel.settingsAccessible.value ?: false
        }*/

        // setup appBar and connect to navigationController
        val toolBar = findViewById<Toolbar>(R.id.topAppBar)
        if(title.isNotEmpty())
            toolBar.title = title
        setSupportActionBar(toolBar)
        supportActionBar?.subtitle = subTitle
        val navController = findNavController(R.id.navHostFragment)
        setupActionBarWithNavController(navController)
    }
}