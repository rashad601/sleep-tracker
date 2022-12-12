package com.sleeptrack.main.sleepdetail

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sleeptrack.database.SleepDatabase
import com.sleeptrack.databinding.ActivitySleepDetailBinding


class SleepDetailActivity : AppCompatActivity() {

    private lateinit var sleepDetailBinding: ActivitySleepDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sleepDetailBinding = ActivitySleepDetailBinding.inflate(layoutInflater)

        setContentView(sleepDetailBinding.root)

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val id = intent.getLongExtra("ID", 0)


        val application = requireNotNull(this).application

        // Create an instance of the ViewModel Factory.
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(id, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val sleepDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(SleepDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        sleepDetailBinding.sleepDetailViewModel = sleepDetailViewModel

        // binding.setLifecycleOwner(this)
        sleepDetailBinding.lifecycleOwner = this

        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
        sleepDetailViewModel.navigateToSleepTracker.observe(this, Observer {
            if (it == true) { // Observed state is true.
                onBackPressed()
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepDetailViewModel.doneNavigating()
            }
        })


        sleepDetailBinding.goBack.setOnClickListener {
            onBackPressed()
        }

    }
}