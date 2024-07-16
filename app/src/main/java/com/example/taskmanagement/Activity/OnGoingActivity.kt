package com.example.taskmanagement.Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagement.Adapter.OngoingDialogUtils
import com.example.taskmanagement.Adapter.TaskAdapter
import com.example.taskmanagement.Models.Ongoing
import com.example.taskmanagement.Models.Task
import com.example.taskmanagement.R
import com.example.taskmanagement.ViewModel.OnGoingViewModel
import com.example.taskmanagement.databinding.ActivityOnGoingBinding

class OnGoingActivity : AppCompatActivity() {
    private val ongoingDialogUtils = OngoingDialogUtils()
    private lateinit var binding: ActivityOnGoingBinding
    private val ongoingViewModel: OnGoingViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private var ONGOING_ID : String = ""
    private var ONGOING : Ongoing = Ongoing()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnGoingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24) // Set custom back arrow icon if needed
        }


        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        taskAdapter = TaskAdapter(
            mutableListOf(),
            onItemClick = {
                task -> showEditTaskDialog(task)
            },
            onItemChecked = {
                task -> checkedTask(task)
            },
            onItemRemove = {
                task -> showRemoveTaskDialog(task)
            })
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(this@OnGoingActivity)
            adapter = taskAdapter
        }

        // Observe ongoing tasks LiveData from ViewModel
        ongoingViewModel.ongoingDetailLiveData.observe(this) { onGoing ->
            taskAdapter.updateTasks(onGoing.tasks)
        }

        // Check if intent contains extras
        if (intent.hasExtra("ongoing_id")) {
            ONGOING_ID = intent.getStringExtra("ongoing_id") ?: ""
            Log.e(TAG, "onCreate: $ONGOING_ID")

            // Fetch ongoing task by ID from ViewModel
            ongoingViewModel.fetchOngoingTaskById(ONGOING_ID)

            // Observe changes in ongoingDetailLiveData to update UI when data is available
            ongoingViewModel.ongoingDetailLiveData.observe(this) { ongoing ->
                if (ongoing != null) {
                    ONGOING = ongoing
                    val completedTasks = ongoing.tasks.filter { it.done }
                    binding.textRate.text = "${completedTasks.size}/ ${ongoing.tasks.size}"
                    binding.textViewOngoingTitle.text = ongoing.title
                    binding.textViewOngoingDetails.text = ongoing.description
                    taskAdapter.updateTasks(ongoing.tasks)
                } else {
                    // Handle case when ongoing is null (optional based on your app logic)
                    // For example, show an error message or take appropriate action
                }
            }
        }

        binding.btnAddTask.setOnClickListener {
            showAddTaskDialog()
        }
        binding.btnEditOngoing.setOnClickListener{
            ongoingDialogUtils.showDialogEditOngoing(view = binding.root, onEditOngoing = { ongoing ->
                editOngoing(ongoing)


            },ONGOING)
        }
        binding.btnRemoveOngoing.setOnClickListener {
            showRemoveOngoingDialog(ONGOING)
        }
    }

    private fun checkedTask(task: Task) {
        ongoingViewModel.updateTaskDoneStatus(ONGOING_ID,task.id,task.done,
            onError = {

            }, onComplete = {

            })
    }
    private fun removeTask(task: Task){
        ongoingViewModel.removeTask(ONGOING_ID,task.id, onComplete = {
            Toast.makeText(this, "Task removed successfully", Toast.LENGTH_SHORT).show()

        }, onError = {
            Toast.makeText(this, "Task removed fail", Toast.LENGTH_SHORT).show()

        })
    }
    private fun showRemoveOngoingDialog(ongoing: Ongoing) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Confirm Removal")
            setMessage("Are you sure you want to remove this task?")
            setPositiveButton("Yes") { dialog, which ->
                removeOngoing(ongoing)
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun showRemoveTaskDialog(task: Task) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Confirm Removal")
            setMessage("Are you sure you want to remove this task?")
            setPositiveButton("Yes") { dialog, which ->
                removeTask(task)
            }
            setNegativeButton("Cancel") { dialog, which ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_task, null)
        val taskName = dialogLayout.findViewById<EditText>(R.id.editTextTaskName)
        val taskDescription = dialogLayout.findViewById<EditText>(R.id.editTextTaskDescription)

        builder.setView(dialogLayout)
            .setPositiveButton("ADD") { dialogInterface, _ ->
                val name = taskName.text.toString()
                val description = taskDescription.text.toString()
                onTaskAdded(name, description)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }
    private fun showEditTaskDialog(task : Task) {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_task, null)
        val taskName = dialogLayout.findViewById<EditText>(R.id.editTextTaskName)
        val taskDescription = dialogLayout.findViewById<EditText>(R.id.editTextTaskDescription)
        taskName.setText(task.name)
        taskDescription.setText(task.description)
        builder.setView(dialogLayout)
            .setPositiveButton("EDIT") { dialogInterface, _ ->
                val name = taskName.text.toString()
                val description = taskDescription.text.toString()
                onTaskEdit(task.id ,name, description,task.done)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }
    private fun onTaskAdded(taskName: String, taskDescription: String) {
        val task = Task("unique_task_id", taskName, taskDescription, false)
        ongoingViewModel.addTask(ONGOING_ID, task,
            onComplete = {
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
            },
            onError = { exception ->
                Toast.makeText(this, "Failed to add task: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun onTaskEdit(taskId: String, taskName: String, taskDescription: String, done: Boolean) {
        val task = Task(taskId, taskName, taskDescription, done)
        ongoingViewModel.updateTask(ONGOING_ID, task,
            onComplete = {
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                taskAdapter.notifyDataSetChanged()
            },
            onError = { exception ->
                Toast.makeText(this, "Failed to add task: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun removeOngoing(ongoing: Ongoing){
        ongoingViewModel.removeOngoing(ONGOING_ID, onComplete = {
            Toast.makeText(this, "Ongoing removed successfully", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }, onError = {
            Toast.makeText(this, "Ongoing removed fail", Toast.LENGTH_SHORT).show()

        });
    }
    private fun editOngoing(ongoing: Ongoing){
        ongoingViewModel.edit(ongoing, onComplete = {
            Toast.makeText(this, "Ongoing edited successfully", Toast.LENGTH_SHORT).show()
            binding.textViewOngoingTitle.text = ongoing.title
            binding.textViewOngoingDetails.text = ongoing.description
        }, onError = {
            Toast.makeText(this, "Ongoing edited fail", Toast.LENGTH_SHORT).show()
        });
    }
}
