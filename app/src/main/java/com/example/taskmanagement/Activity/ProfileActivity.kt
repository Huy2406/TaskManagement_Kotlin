package com.example.taskmanagement.Activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagement.Adapter.TaskDoneAdapter
import com.example.taskmanagement.ViewModel.OnGoingViewModel
import com.example.taskmanagement.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val ongoingViewModel: OnGoingViewModel by viewModels()
    private lateinit var taskDoneAdapter: TaskDoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize TaskDoneAdapter with an empty list
        taskDoneAdapter = TaskDoneAdapter(mutableListOf())
        // Set up RecyclerView
        binding.viewTeam.apply {
            Log.d(TAG, "onCreate: ${23423423}")
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = taskDoneAdapter
        }
        ongoingViewModel.getAllDoneTasksAcrossOngoing()
        // Observe LiveData from ViewModel
        ongoingViewModel.ongoingTasksDoneLiveData.observe(this) { tasks ->
            Log.d(TAG, "onCreate: ${tasks.size}")
            taskDoneAdapter.updateTasks(tasks)
        }
    }
}
