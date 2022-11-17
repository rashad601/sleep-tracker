package com.sleeptrack.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao{

    @Insert
    fun insert(sleepNight: SleepNight)

    @Update
    fun update(sleepNight: SleepNight)

    @Query("SELECT * FROM daily_sleep_quality_table WHERE night_id=:key")
    fun get(key: Long): SleepNight?

    @Query("DELETE FROm daily_sleep_quality_table")
    fun clear()

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY night_id DESC")
    fun getAllNights():LiveData<List<SleepNight>>

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY night_id DESC LIMIT 1")
    fun getTonight(): SleepNight?

    @Query("SELECT * from daily_sleep_quality_table WHERE night_id=:key")
    fun getNightWithId(key: Long): LiveData<SleepNight>

    @Delete
    fun delete(night: SleepNight)

    @Delete
    fun deleteAllNights(nights: List<SleepNight>): Int
}