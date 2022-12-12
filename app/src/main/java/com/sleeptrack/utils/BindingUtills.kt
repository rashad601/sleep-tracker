package com.sleeptrack.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sleeptrack.R
import com.sleeptrack.database.SleepNight
import java.lang.String.format
import java.text.MessageFormat.format
import java.text.SimpleDateFormat

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) {
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}

@BindingAdapter("sleepStartDurationFormatted")
fun TextView.setSleepStartDurationFormatted(item: SleepNight?) {
    item?.let {
        text = "Start: "+SimpleDateFormat("EEE MMM, yyyy hh:mm:ss a").format( item.startTimeMilli)
    }
}

@BindingAdapter("sleepEndDurationFormatted")
fun TextView.setsleepEndDurationFormatted(item: SleepNight?) {
    item?.let {
        text ="End: "+SimpleDateFormat("EEE MMM, yyyy hh:mm:ss a").format( item.endTimeMilli);
    }
}

@BindingAdapter("sleepFeedbackFormatted")
fun TextView.setsleepFeedbackFormatted(item: SleepNight?) {
    item?.let {
        if(!it.feedback.trim().isNullOrEmpty() && !it.feedback.equals("null",ignoreCase = true))
        text = "Feedback : "+it.feedback
    }
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?) {
    item?.let {
        setImageResource(
            when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            }
        )
    }
}

@BindingAdapter("setVisible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE
    else View.INVISIBLE
}