package com.sleeptrack.about

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sleeptrack.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var activityAboutBinding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAboutBinding = ActivityAboutBinding.inflate(layoutInflater)

        setContentView(activityAboutBinding.root)

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        activityAboutBinding.goBack.setOnClickListener {
            onBackPressed()
        }
    }
}