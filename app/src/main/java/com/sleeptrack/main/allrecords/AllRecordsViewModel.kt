package com.sleeptrack.main.allrecords

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sleeptrack.database.SleepDatabaseDao
import com.sleeptrack.database.SleepNight
import com.sleeptrack.utils.formatNights
import kotlinx.coroutines.*

class AllRecordsViewModel(val database: SleepDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var tonight = MutableLiveData<SleepNight?>()

    private val _nights = database.getAllNights()
    val nights: LiveData<List<SleepNight>>
        get() = _nights

    val nightsString = Transformations.map(_nights) {
        formatNights(it, application.resources)
    }


    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>(false)
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    init {
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
