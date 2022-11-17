package com.sleeptrack.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sleeptrack.database.SleepDatabase
import com.sleeptrack.databinding.FragmentSleepTrackerBinding


class SleepTrackerFragment : Fragment() {

    private lateinit var binding: FragmentSleepTrackerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSleepTrackerBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application

        val datasource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(datasource, application)

        val sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        binding.sleepTrackerViewModel = sleepTrackerViewModel

        binding.setLifecycleOwner(this)

        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToRecordSleepFragment(night.nightId)
                )
                sleepTrackerViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}