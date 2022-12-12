package com.sleeptrack.main.allrecords

import android.content.res.Resources
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.sleeptrack.R
import com.sleeptrack.database.SleepDatabase
import com.sleeptrack.databinding.ActivityAllRecordsGraphBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)


class AllRecordsGraphActivity : AppCompatActivity() {

    private lateinit var activityAllRecordsGraphBinding: ActivityAllRecordsGraphBinding

    private lateinit var sleepTrackerViewModel: AllRecordsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAllRecordsGraphBinding = ActivityAllRecordsGraphBinding.inflate(layoutInflater)

        setContentView(activityAllRecordsGraphBinding.root)

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val application = requireNotNull(this).application

        val datasource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = AllRecordsViewModelFactory(datasource, application)

        sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(AllRecordsViewModel::class.java)

        activityAllRecordsGraphBinding.setLifecycleOwner(this)


        activityAllRecordsGraphBinding.goBack.setOnClickListener {
            onBackPressed()
        }

        sleepTrackerViewModel.nights.observe(this, Observer {
            it?.let { sleepNights ->
                val list = ArrayList<String>()
                val allGrapohData = ArrayList<Entry>()
                for (i in 0 until sleepNights.size) {
                    allGrapohData.add(
                        Entry(
                            i.toFloat(),
                            TimeUnit.MILLISECONDS.toMinutes(
                                (sleepNights.get(i).endTimeMilli - sleepNights.get(
                                    i
                                ).startTimeMilli)
                            ).toFloat()
                        )
                    )

                    list.add(
                        SimpleDateFormat("dd EEE", Locale.getDefault()).format(
                            sleepNights.get(
                                i
                            ).startTimeMilli
                        ).toString()
                    )
                }
                populateGraph(allGrapohData, list.toTypedArray())
            }
        })

    }


    fun populateGraph(dataSet: ArrayList<Entry>, horizontalList: Array<String>) {
        var linedataset = LineDataSet(dataSet, "Days")
        linedataset.color = resources.getColor(R.color.primaryDarkColor)
        linedataset.circleRadius = 5f
        linedataset.circleColors = mutableListOf(resources.getColor(R.color.primaryDarkColor))
        linedataset.setDrawFilled(true)
        linedataset.fillColor = resources.getColor(R.color.primaryColor)
        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        var datasets = ArrayList<ILineDataSet>()
        datasets.add(linedataset)

        activityAllRecordsGraphBinding.myLineChart.data = LineData(datasets)
        activityAllRecordsGraphBinding.myLineChart.invalidate()
        activityAllRecordsGraphBinding.myLineChart.getAxisLeft().setDrawGridLines(false)
        activityAllRecordsGraphBinding.myLineChart.getXAxis().setDrawGridLines(false)
        activityAllRecordsGraphBinding.myLineChart.getAxisRight().setDrawGridLines(false)
        activityAllRecordsGraphBinding.myLineChart.getAxisRight().setEnabled(false)
        activityAllRecordsGraphBinding.myLineChart.setBackgroundColor(resources.getColor(R.color.white))
        activityAllRecordsGraphBinding.myLineChart.animateXY(1000, 1000, Easing.EaseInCubic)
        configureChartAppearance(horizontalList)
    }

    private fun configureChartAppearance(days: Array<String>) {
        activityAllRecordsGraphBinding.myLineChart.getDescription().setEnabled(false)
        val xAxis: XAxis = activityAllRecordsGraphBinding.myLineChart.getXAxis()
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return days[value.toInt()]
            }
        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 7
    }

    fun convertDurationToFormatted(
        startTimeMilli: Long,
        endTimeMilli: Long,
        res: Resources
    ): String {
        val durationMilli = endTimeMilli - startTimeMilli
        val weekdayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)
        return when {
            durationMilli < ONE_MINUTE_MILLIS -> {
                val seconds = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
                res.getString(R.string.seconds_length, seconds, weekdayString)
            }
            durationMilli < ONE_HOUR_MILLIS -> {
                val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
                res.getString(R.string.minutes_length, minutes, weekdayString)
            }
            else -> {
                val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
                res.getString(R.string.hours_length, hours, weekdayString)
            }
        }
    }

}