package com.example.harvester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.harvester.framework.ui.main.MainFragment
import ru.scancity.enterprise.ui.allert.action.UIAlertAction


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

        UIAlertAction.connectTo(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
