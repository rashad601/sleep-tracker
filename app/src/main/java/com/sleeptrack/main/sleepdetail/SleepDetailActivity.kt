package com.sleeptrack.main.sleepdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.sleeptrack.R
import com.sleeptrack.databinding.ActivityAboutBinding
import com.sleeptrack.databinding.ActivitySleepDetailBinding

class SleepDetailActivity : AppCompatActivity() {

    private lateinit var sleepDetailBinding: ActivitySleepDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sleepDetailBinding = ActivitySleepDetailBinding.inflate(layoutInflater)

        setContentView(sleepDetailBinding.root)

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sleepDetailBinding.goBack.setOnClickListener{
            onBackPressed()
        }

    }
}