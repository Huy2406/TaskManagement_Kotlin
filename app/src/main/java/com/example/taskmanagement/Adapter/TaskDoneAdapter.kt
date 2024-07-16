package com.example.taskmanagement.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.Models.Task
import com.example.taskmanagement.R

class TaskDoneAdapter(    var tasks: MutableList<Task>,
) : RecyclerView.Adapter<TaskDoneAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.text_name_done)
        val descriptionTextView: TextView = itemView.findViewById(R.id.text_description_name)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_done, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.titleTextView.text = task.name
        holder.descriptionTextView.text = task.description
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }




}
