package com.example.taskmanagement.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.Models.Task
import com.example.taskmanagement.R

class TaskAdapter(
    var tasks: MutableList<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onItemChecked: (Task) -> Unit,
    private  val onItemRemove : (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        private val btnEdit: AppCompatButton = itemView.findViewById(R.id.btn_edit)
        private val btnRemove: AppCompatButton = itemView.findViewById(R.id.btn_remove)
        private val layout: LinearLayout = itemView.findViewById(R.id.tasklayout)

        init {
            btnEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(tasks[position])
                }
            }
            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                tasks[position].done = isChecked
                layout.isEnabled = false
                if (position != RecyclerView.NO_POSITION) {
                    onItemChecked(tasks[position])
                }
            }
            btnRemove.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemRemove(tasks[position])
                }
            }
        }


        fun bind(task: Task) {
            checkBoxTask.text = task.name
            if (task.done) {
                layout.isEnabled = false
                layout.isClickable = false
                btnEdit.visibility = View.GONE
//                btnRemove.visibility = View.GONE
                checkBoxTask.isChecked = true
                checkBoxTask.isEnabled = false
            } else {
                layout.setBackgroundResource(0) // Reset background if not done
            }
        }
    }


}
