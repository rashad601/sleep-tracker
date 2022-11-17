package com.sleeptrack.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "night_id")
    var nightId: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    var startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "send_time_milli")
    var endTimeMilli: Long = startTimeMilli,

    @ColumnInfo(name = "quality_rating")
    var sleepQuality: Int = -1,

    @ColumnInfo(name = "feedback")
    var feedback: String = ""

)