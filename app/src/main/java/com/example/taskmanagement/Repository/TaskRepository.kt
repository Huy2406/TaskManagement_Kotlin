package com.example.taskmanagement.Repository

import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository {

    private val db = FirebaseFirestore.getInstance()

    // Update 'done' status of a task in an ongoing task
    fun updateTaskDoneStatus(ongoingId: String, taskId: String, done: Boolean, onComplete: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("ongoing")
            .document(ongoingId)
            .collection("tasks")
            .document(taskId)
            .update("done", done)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onError(it) }
    }
}