package com.sleeptrack.main.home

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sleeptrack.database.SleepDatabase
import com.sleeptrack.databinding.FragmentSleepTrackerBinding


val START_BUZZ_PATTERN =
    longArrayOf(100, 100, 100, 100, 100, 100, 100, 100)
val FEEDBACK_BUZZ_PATTERN =
    longArrayOf(0, 100, 500, 900,)
val STOP_BUZZ_PATTERN = longArrayOf(0, 1000)


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
                buzz(STOP_BUZZ_PATTERN)
                findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToRecordSleepFragment(night.nightId)
                )
                sleepTrackerViewModel.doneNavigating()
            }
        })
        sleepTrackerViewModel.tonight.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                buzz(START_BUZZ_PATTERN)
                binding.startTime.base =
                    SystemClock.elapsedRealtime() - (System.currentTimeMillis() - night.startTimeMilli)
                binding.startTime.start()
            }
        })

        return binding.root
    }

    fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {

                buzzer.vibrate(pattern, -1)
            }
        }
    }
}