package com.example.harvester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.harvester.R
import com.example.harvester.framework.App
import com.example.harvester.framework.ui.main.MainFragment
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.kotlin.delete

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home-> {
                Toast.makeText(this, "Modes", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_scan -> {
                Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_settings-> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}