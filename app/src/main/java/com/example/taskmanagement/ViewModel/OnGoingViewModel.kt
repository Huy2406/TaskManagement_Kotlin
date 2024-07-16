package com.example.taskmanagement.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanagement.Models.Ongoing
import com.example.taskmanagement.Models.Task
import com.example.taskmanagement.Repository.OngoingRepository

class OnGoingViewModel(private val repository: OngoingRepository) : ViewModel() {

    // LiveData for ongoing tasks list

    private val _ongoingTasksLiveData = MutableLiveData<List<Ongoing>>()
    val ongoingTasksLiveData: LiveData<List<Ongoing>>
        get() = _ongoingTasksLiveData

    // LiveData for ongoing task detail
    private val _ongoingDetailLiveData = MutableLiveData<Ongoing>()
    val ongoingDetailLiveData: LiveData<Ongoing>
        get() = _ongoingDetailLiveData


    private val _ongoingTasksDoneLiveData = MutableLiveData<List<Task>>()
    val ongoingTasksDoneLiveData: LiveData<List<Task>>
        get() = _ongoingTasksDoneLiveData
    constructor() : this(OngoingRepository())

    // Method to fetch all ongoing tasks
    fun fetchOngoingTasks() {
        _ongoingTasksLiveData.value = emptyList()
        repository.getAll(
            onSuccess = {ongoingList ->
                _ongoingTasksLiveData.value = ongoingList
            },
            onError = { exception ->
                // Handle error scenario
            }
        )
    }

    // Method to fetch ongoing task by ID
    fun fetchOngoingTaskById(id: String) {
        repository.getById(
             id,
            onSuccess = { ongoing ->
                _ongoingDetailLiveData.value = ongoing!!
            },
            onError = { exception ->
                // Handle error scenario
            }
        )
    }

    // Method to search ongoing tasks by title
    fun searchByTitle(title: String) {
        if (title == ""){
            fetchOngoingTasks()
        }
        repository.searchByTitle(
            title = title,
            onSuccess = { ongoingTasks ->
                _ongoingTasksLiveData.value = ongoingTasks
            },
            onError = { exception ->
                // Handle error scenario
            }
        )
    }

    // Method to save or update an ongoing task
    fun add(ongoing: Ongoing, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        repository.addOngoing(ongoing,
            onComplete = {
                onComplete()
                // Automatically refresh ongoing tasks after save/update
                fetchOngoingTasks()
            },
            onError = {
                onError(it)
            }
        )
    }
    fun edit(ongoing: Ongoing, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        repository.editOngoing(ongoing,
            onComplete = {
                onComplete()
                // Automatically refresh ongoing tasks after save/update
                fetchOngoingTasks()
            },
            onError = {
                onError(it)
            }
        )
    }

    // Method to add a task to an ongoing task
    fun addTask(ongoingId: String, task: Task, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        repository.addTask(
            ongoingId = ongoingId,
            task = task,
            onComplete = {
                onComplete()
                // Automatically refresh ongoing tasks after adding task
                fetchOngoingTaskById(ongoingId)
            },
            onError = {
                onError(it)
            }
        )
    }

    // Method to update the 'done' status of a task within an ongoing task
    fun updateTaskDoneStatus(ongoingId: String, taskId: String, done: Boolean, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        repository.updateTaskDoneStatus(
            ongoingId = ongoingId,
            taskId = taskId,
            done = done,
            onComplete = {
                onComplete()
                // Automatically refresh ongoing tasks after updating task status
                fetchOngoingTaskById(ongoingId)
            },
            onError = {
                onError(it)
            }
        )
    }

    // Method to update details of a task within an ongoing task
    fun updateTask(ongoingId: String, task: Task, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        repository.updateTask(
            ongoingId = ongoingId,
            task.id,
            task.name,
            task.description,
            onComplete = {
                onComplete()
                // Automatically refresh ongoing tasks after updating task details
                fetchOngoingTaskById(ongoingId)
            },
            onError = {
                onError(it)
            }
        )
    }
    fun removeTask(ongoingId: String, taskId: String,onComplete: () -> Unit, onError: (Exception) -> Unit){
        repository.removeTask(ongoingId,taskId, onSuccess = {
            onComplete()
            fetchOngoingTaskById(ongoingId)
        },onError);
    }
    fun removeOngoing(ongoingId: String,onComplete: () -> Unit,onError: (Exception) -> Unit){
        repository.removeOngoing(ongoingId,onComplete,onError)
    }
    fun getAllDoneTasksAcrossOngoing() {
        repository.fetchAllDoneTasks(
            onSuccess = { tasks ->
                _ongoingTasksDoneLiveData.value = tasks
                for (task in tasks) {
                    Log.d(TAG, "Task ID: ${task.id}, Name: ${task.name}, Description: ${task.description}, Done: ${task.done}")
                }
            },
            onError = { exception ->
                Log.e(TAG, "Error fetching tasks: $exception")
                // Handle error
            }
        )
    }
}
