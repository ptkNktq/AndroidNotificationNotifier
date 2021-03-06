package me.nya_n.notificationnotifier.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.nya_n.notificationnotifier.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}