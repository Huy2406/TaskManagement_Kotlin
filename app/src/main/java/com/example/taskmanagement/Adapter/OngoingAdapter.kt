package com.example.taskmanagement.Adapter

import android.content.ContentValues.TAG
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmanagement.Models.Ongoing
import com.example.taskmanagement.Models.Task
import com.example.taskmanagement.R
import com.google.firebase.database.core.Tag
import java.text.SimpleDateFormat
import java.util.Locale

class OngoingAdapter(private val onItemClick: (Ongoing) -> Unit) :
    RecyclerView.Adapter<OngoingAdapter.ViewHolder>() {

    private var ongoingList = emptyList<Ongoing>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.titleTxt)
        private val date: TextView = itemView.findViewById(R.id.dateTxt)
        private val progressBarPercent: TextView = itemView.findViewById(R.id.percentTxt)
        private val progressTxt: TextView = itemView.findViewById(R.id.progressTxt)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val pic: ImageView = itemView.findViewById(R.id.pic)
        private val layout: ConstraintLayout = itemView.findViewById(R.id.layout)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(ongoingList[position])
                }
            }
        }

        fun bind(item: Ongoing) {
            title.text = item.title
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            try {
                val parsedDate = inputFormat.parse(item.date)
                val formattedDate = outputFormat.format(parsedDate!!)
                date.text = formattedDate
            } catch (e: Exception) {
                e.printStackTrace()
                date.text = item.date // Fallback to original date if parsing fails
            }
            val progressPercent = calculateCompletionPercentage(item.tasks)
            Log.d(TAG,"${item.title} ${progressPercent}");

            progressBarPercent.text = "$progressPercent%"

            // Load image using Glide
            val drawableResourceId = itemView.context.resources.getIdentifier(
                item.picPath.lowercase(), "drawable", itemView.context.packageName
            )

            Glide.with(itemView.context).load(drawableResourceId).into(pic)

            progressBar.progress = progressPercent

            // Apply background and text colors based on position
            if (adapterPosition % 2 == 0) {
                layout.setBackgroundResource(R.drawable.dark_bg)
                setTextColors(R.color.white)
            } else {
                layout.setBackgroundResource(R.drawable.light_purple_background)
                setTextColors(R.color.dark_purple)
            }
        }

        private fun calculateCompletionPercentage(tasks: List<Task>): Int {
            if (tasks.isEmpty()) {
                return 0
            }
            val completedTasks = tasks.count { it.done }
            Log.d(TAG,"aipjsciajcsc ${completedTasks}");
            return (completedTasks / tasks.size) * 100
        }

        private fun setTextColors(colorRes: Int) {
            title.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
            date.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
            progressTxt.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
            progressBarPercent.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
            pic.setColorFilter(ContextCompat.getColor(itemView.context, colorRes), PorterDuff.Mode.SRC_IN)
            progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, colorRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_ongoing, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ongoingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ongoingList[position])
    }

    fun setData(ongoingList: List<Ongoing>) {
        this.ongoingList = ongoingList
        notifyDataSetChanged()
    }
}
