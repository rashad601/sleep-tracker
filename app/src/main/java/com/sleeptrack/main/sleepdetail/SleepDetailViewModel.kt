package com.sleeptrack.main.sleepdetail

import androidx.lifecycle.*
import com.sleeptrack.database.SleepDatabaseDao
import com.sleeptrack.database.SleepNight

class SleepDetailViewModel(private val sleepNightKey: Long = 0L, dataSource: SleepDatabaseDao) :
    ViewModel() {

    /**
     * Hold a reference to SleepDatabase via its SleepDatabaseDao.
     */
    val database = dataSource

    //
    //     As Given in the Course it was this below structure
    //
    //    private val night = MediatorLiveData<SleepNight>()
    //
    //    fun getNight() = night
    //
    //    init {
    //        night.addSource(database.getNightWithId(sleepNightKey), night::setValue)
    //    }

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