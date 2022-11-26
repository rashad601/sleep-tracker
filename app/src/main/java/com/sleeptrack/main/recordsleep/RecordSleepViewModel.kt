package com.sleeptrack.main.recordsleep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sleeptrack.database.SleepDatabaseDao
import kotlinx.coroutines.*

class RecordSleepViewModel(private val sleepNightKey: Long = 0L, val database: SleepDatabaseDao) :
    ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val feeback = MutableLiveData<String>("")
    val quality = MutableLiveData<Int>(3)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }


    fun save() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val tonight = database.get(sleepNightKey) ?: return@withContext
                tonight.feedback =
                    if (!feeback.value.toString().trim().isNullOrEmpty()) feeback.value.toString()
                        .trim() else ""
                tonight.sleepQuality = quality.value!!
                database.update(tonight)
            }
            _navigateToSleepTracker.value = true
        }
    }

    fun setFeelingQuality(i: Int) {
        quality.value = i
    }

}