package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.searchActivity.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bSearch = findViewById<Button>(R.id.bSearch)
        val bLibrary = findViewById<Button>(R.id.bLibrary)
        val bSettings = findViewById<Button>(R.id.bSettings)

        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        bSearch.setOnClickListener(buttonSearchClickListener)

        bLibrary.setOnClickListener {
            val intent = Intent(this@MainActivity, LibraryActivity::class.java)
            startActivity(intent)
        }

        bSettings.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}