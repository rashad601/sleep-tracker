package com.sleeptrack.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sleeptrack.database.SleepDatabaseDao
import com.sleeptrack.database.SleepNight
import com.sleeptrack.utils.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(val database: SleepDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    private val _nights = database.getAllNights()
    val nights: LiveData<List<SleepNight>>
        get() = _nights

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = _navigateToSleepQuality

    private val _navigateToSleepDataQuality = MutableLiveData<Long?>()
    val navigateToSleepDataQuality: LiveData<Long?>
        get() = _navigateToSleepDataQuality

    val nightsString = Transformations.map(_nights) {
        formatNights(it, application.resources)
    }

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }
    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>(false)
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {

            val newNight = SleepNight()

            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }

    }

    private suspend fun insert(newNight: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(newNight)
        }
    }


    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }


    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
        tonight.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onSleepNightClicked(it: Long) {
        _navigateToSleepDataQuality.value = it
    }

    fun onSleepDataQualityNavigated() {
        _navigateToSleepDataQuality.value = null
    }


}
