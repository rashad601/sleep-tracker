package com.sleeptrack.main.recordsleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sleeptrack.database.SleepDatabaseDao

class RecordSleepViewModelFactory(
    private val sleepNightKey: Long,
    private val dataSource: SleepDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordSleepViewModel::class.java)) {
            return RecordSleepViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}