package com.sleeptrack.main.allrecords

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleeptrack.database.SleepNight
import com.sleeptrack.databinding.ItemSleepBinding
import com.sleeptrack.utils.setSleepDurationFormatted
import com.sleeptrack.utils.setSleepImage


class AllSleepRecordRecyclerviewAdapter(
    private val context: Context,
    private val listener: OnPositionClick
) : RecyclerView.Adapter<AllSleepRecordRecyclerviewAdapter.ViewHolder>() {

    private var mainList = mutableListOf<SleepNight>()

    fun setData(list: ArrayList<SleepNight>) {
        mainList = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemSleepBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mainList[position]) {

                binding.datetime.setSleepDurationFormatted(this)

                binding.image.setSleepImage(this)

                binding.feedback.text = this.feedback

                binding.cardview.setOnClickListener {
                    listener.onItemClick(this@with)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    interface OnPositionClick {
        fun onItemClick(position: SleepNight)
    }
}