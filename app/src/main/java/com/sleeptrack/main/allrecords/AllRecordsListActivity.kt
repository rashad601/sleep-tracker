package com.sleeptrack.main.allrecords

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sleeptrack.databinding.ActivityAllRecordsListBinding

class AllRecordsListActivity : AppCompatActivity() {

    private lateinit var activityAllRecordsBinding: ActivityAllRecordsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAllRecordsBinding = ActivityAllRecordsListBinding.inflate(layoutInflater)

        setContentView(activityAllRecordsBinding.root)

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        activityAllRecordsBinding.goBack.setOnClickListener {
            onBackPressed()
        }

    }
}