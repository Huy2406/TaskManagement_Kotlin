package com.example.taskmanagement.Repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.taskmanagement.Models.Ongoing
import com.example.taskmanagement.Models.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class OngoingRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("ongoing")

    // Create or update an ongoing task
//    fun saveOrUpdate(ongoing: Ongoing, onComplete: () -> Unit, onError: (Exception) -> Unit) {
//        if (ongoing.id.isEmpty()) {
//            ongoing.id = db.collection("ongoing").document().id
//        }
//
//        // Save the ongoing object with the generated ID
//        collection.document(ongoing.id)
//            .set(ongoing)
//            .addOnSuccessListener {
//                saveTasks(ongoing.id, ongoing.tasks,
//                    onComplete = {
//                        onComplete()
//                    },
//                    onError = {
//                        onError(it)
//                    }
//                )
//            }
//            .addOnFailureListener { onError(it) }
//    }
    fun addOngoing(ongoing: Ongoing, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        // Generate ID if it's empty
        if (ongoing.id.isEmpty()) {
            ongoing.id = db.collection("ongoing").document().id
        }

        // Save the ongoing object with the generated ID
        collection.document(ongoing.id)
            .set(ongoing)
            .addOnSuccessListener {
                saveTasks(ongoing.id, ongoing.tasks,
                    onComplete = {
                        onComplete()
                    },
                    onError = {
                        onError(it)
                    }
                )
            }
            .addOnFailureListener { onError(it) }
    }
    fun editOngoing(ongoing: Ongoing, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        // Update the ongoing object
        collection.document(ongoing.id)
            .set(ongoing)
            .addOnSuccessListener {
                saveTasks(ongoing.id, ongoing.tasks,
                    onComplete = {
                        onComplete()
                    },
                    onError = {
                        onError(it)
                    }
                )
            }
            .addOnFailureListener { onError(it) }
    }

    // Save tasks associated with an ongoing task
    private fun saveTasks(ongoingId: String, tasks: List<Task>, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        val tasksCollection = collection.document(ongoingId).collection("tasks")

        // Clear existing tasks first
        tasksCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val batch = db.batch()
                querySnapshot.documents.forEach { document ->
                    batch.delete(document.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        // Now add new tasks
                        tasks.forEach { task ->
                            tasksCollection.add(task)
                                .addOnFailureListener { onError(it) }
                        }
                        onComplete()
                    }
                    .addOnFailureListener { onError(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    // Add a single task to an ongoing task
    fun addTask(ongoingId: String, task: Task, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "addTask: ${ongoingId}")
        collection.document(ongoingId).collection("tasks")
            .add(task)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onError(it) }
    }

    // Retrieve all ongoing tasks
    fun getAll(onSuccess: (List<Ongoing>) -> Unit, onError: (Exception) -> Unit) {
        collection.get()
            .addOnSuccessListener { result ->
                val list = result.toObjects(Ongoing::class.java)
                onSuccess(list)
            }
            .addOnFailureListener { onError(it) }
    }

    // Retrieve an ongoing task by ID
    fun getById(ongoingId: String, onSuccess: (Ongoing?) -> Unit, onError: (Exception) -> Unit) {
        collection.document(ongoingId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val ongoing = documentSnapshot.toObject(Ongoing::class.java)
                    ongoing?.id = documentSnapshot.id
                    fetchTasksForOngoing(ongoingId,
                        onSuccess = { tasks ->
                            ongoing?.tasks = tasks
                            onSuccess(ongoing)
                        },
                        onError = { onError(it) }
                    )
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { onError(it) }
    }

    // Fetch tasks associated with an ongoing task
     fun fetchTasksForOngoing(ongoingId: String, onSuccess: (List<Task>) -> Unit, onError: (Exception) -> Unit) {
        collection.document(ongoingId).collection("tasks")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val tasks = mutableListOf<Task>()
                for (document in querySnapshot.documents) {
                    val task = document.toObject(Task::class.java)
                    task?.id = document.id
                    task?.let { tasks.add(it) }
                }
                onSuccess(tasks)
            }
            .addOnFailureListener { onError(it) }
    }

    // Search for ongoing tasks by title
    fun searchByTitle(title: String, onSuccess: (List<Ongoing>) -> Unit, onError: (Exception) -> Unit) {
        collection.whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { result ->
                val list = result.toObjects(Ongoing::class.java)
                onSuccess(list)
            }
            .addOnFailureListener { onError(it) }
    }

    // Update the 'done' status of a task within an ongoing task
    fun updateTaskDoneStatus(ongoingId: String, taskId: String, done: Boolean, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        collection.document(ongoingId).collection("tasks").document(taskId)
            .update("done", done)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onError(it) }
    }

    // Update the details of a task within an ongoing task
    fun updateTask(ongoingId: String, taskId: String, taskName: String, taskDescription: String, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        collection.document(ongoingId).collection("tasks").document(taskId)
            .update(mapOf(
                "name" to taskName,
                "description" to taskDescription
            ))
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onError(it) }
    }
    fun removeTask(ongoingId: String,taskId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        collection.document(ongoingId).collection("tasks").document(taskId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
    fun removeOngoing(ongoingId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        collection.document(ongoingId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
    fun fetchAllDoneTasks(onSuccess: (List<Task>) -> Unit, onError: (Exception) -> Unit) {
        val allTasks = mutableListOf<Task>()
        Log.d(TAG,"okeeofkev")
        collection.get()
            .addOnSuccessListener { ongoingSnapshot ->
                val tasksQueries = mutableListOf<Query>()
                ongoingSnapshot.documents.forEach { ongoingDoc ->
                    val tasksQuery = ongoingDoc.reference.collection("tasks")
                        .whereEqualTo("done", true)
                    tasksQueries.add(tasksQuery)
                }

                // Execute all queries in parallel
                val queryTasks = tasksQueries.map { query ->
                    query.get()
                }

                // Wait for all queries to complete
                Tasks.whenAllSuccess<QuerySnapshot>(queryTasks)
                    .addOnSuccessListener { snapshots ->
                        snapshots.forEach { snapshot ->
                            snapshot.documents.forEach { doc ->
                                val task = doc.toObject(Task::class.java)
                                task?.id = doc.id
                                task?.let { allTasks.add(it) }
                            }
                        }
                        onSuccess(allTasks)
                    }
                    .addOnFailureListener { exception ->
                        onError(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }


}
