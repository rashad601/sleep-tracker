package com.sleeptrack.main.recordsleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sleeptrack.databinding.FragmentRecordSleepBinding


class RecordSleepFragment : Fragment() {

    private lateinit var binding: FragmentRecordSleepBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordSleepBinding.inflate(inflater, container, false)


        return binding.root
    }

}