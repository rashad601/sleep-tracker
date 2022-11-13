package com.sleeptrack.main.allrecords

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sleeptrack.databinding.ActivityAllRecordsGraphBinding

class AllRecordsGraphActivity : AppCompatActivity() {

    private lateinit var activityAllRecordsGraphBinding: ActivityAllRecordsGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAllRecordsGraphBinding = ActivityAllRecordsGraphBinding.inflate(layoutInflater)

        setContentView(activityAllRecordsGraphBinding.root)

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        activityAllRecordsGraphBinding.goBack.setOnClickListener {
            onBackPressed()
        }

    }
}