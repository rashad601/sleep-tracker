package com.sleeptrack.main.sleepdetail

import androidx.lifecycle.*
import com.sleeptrack.database.SleepDatabaseDao
import com.sleeptrack.database.SleepNight

class SleepDetailViewModel(private val sleepNightKey: Long = 0L, dataSource: SleepDatabaseDao) :
    ViewModel() {

    val database = dataSource

    private val _night = MediatorLiveData<SleepNight>()
    val night: LiveData<SleepNight>
        get() = _night

    init {
        _night.addSource(database.getNightWithId(sleepNightKey), Observer {
            _night.value = it
        })
    }

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onClose() {
        _navigateToSleepTracker.value = true
    }
}