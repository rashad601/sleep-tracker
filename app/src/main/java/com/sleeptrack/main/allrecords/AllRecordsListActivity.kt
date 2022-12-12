package com.sleeptrack.main.allrecords

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.sleeptrack.R
import com.sleeptrack.database.SleepDatabase
import com.sleeptrack.database.SleepNight
import com.sleeptrack.databinding.ActivityAllRecordsListBinding

class AllRecordsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllRecordsListBinding

    private lateinit var sleepTrackerViewModel: AllRecordsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllRecordsListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val application = requireNotNull(this).application

        val datasource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = AllRecordsViewModelFactory(datasource, application)

        sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(AllRecordsViewModel::class.java)

        binding.sleepTrackerViewModel = sleepTrackerViewModel

        binding.setLifecycleOwner(this)

        binding.goBack.setOnClickListener {
            onBackPressed()
        }


        val recyclerviewAdapter = AllSleepRecordRecyclerviewAdapter(this,
            object : AllSleepRecordRecyclerviewAdapter.OnPositionClick {
                override fun onItemClick(story: SleepNight) {

                }

            })

        binding.sleepRecyclerview.adapter = recyclerviewAdapter


        sleepTrackerViewModel.nights.observe(this, Observer {
            it?.let{
                recyclerviewAdapter.setData(it as ArrayList<SleepNight>)
            }
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                   findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

    }
}