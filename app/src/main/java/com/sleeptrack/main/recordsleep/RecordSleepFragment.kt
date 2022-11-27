package com.sleeptrack.main.recordsleep

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hadi.emojiratingbar.EmojiRatingBar
import com.hadi.emojiratingbar.RateStatus
import com.sleeptrack.database.SleepDatabase
import com.sleeptrack.databinding.FragmentRecordSleepBinding
import com.sleeptrack.main.home.FEEDBACK_BUZZ_PATTERN
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RecordSleepFragment : Fragment() {

    private lateinit var binding: FragmentRecordSleepBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordSleepBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = RecordSleepFragmentArgs.fromBundle(requireArguments())

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = RecordSleepViewModelFactory(arguments.sleepNightKey, dataSource)

        val sleepQualityViewModel =
            ViewModelProvider(this, viewModelFactory).get(RecordSleepViewModel::class.java)

        binding.sleepQualityViewModel = sleepQualityViewModel

        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                lifecycleScope.launch {
                    buzz(FEEDBACK_BUZZ_PATTERN)
                    binding.lavToggle.playAnimation()
                    binding.save.visibility = View.INVISIBLE
                    delay(5000)
                    findNavController().navigate(
                        RecordSleepFragmentDirections.actionRecordSleepFragmentToSleepTrackerFragment()
                    )
                    sleepQualityViewModel.doneNavigating()
                }

            }
        })

        binding.emojiRatingBar.setRateChangeListener(object : EmojiRatingBar.OnRateChangeListener {
            override fun onRateChanged(rateStatus: RateStatus) {

                when (rateStatus) {

                    RateStatus.AWFUL -> {
                        sleepQualityViewModel.setFeelingQuality(1)
                    }
                    RateStatus.BAD -> {
                        sleepQualityViewModel.setFeelingQuality(2)
                    }
                    RateStatus.OKAY -> {
                        sleepQualityViewModel.setFeelingQuality(3)
                    }
                    RateStatus.GOOD -> {
                        sleepQualityViewModel.setFeelingQuality(4)
                    }
                    RateStatus.GREAT -> {
                        sleepQualityViewModel.setFeelingQuality(5)
                    }
                    RateStatus.EMPTY -> {
                        sleepQualityViewModel.setFeelingQuality(0)
                    }

                }
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