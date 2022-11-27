package com.sleeptrack.main.allrecords

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sleeptrack.database.SleepDatabaseDao


class AllRecordsViewModelFactory
    (private val dataSource: SleepDatabaseDao, private val application: Application) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllRecordsViewModel::class.java)) {
            return AllRecordsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}