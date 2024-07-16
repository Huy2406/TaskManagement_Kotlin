package com.example.taskmanagement.Repository

import com.example.taskmanagement.Models.Ongoing
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepository {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("ongoing_tasks")

    //    val items= listOf(
//        Ongoing("1","Chatbot", "July 13,2024", 50, "ongoing1"),
//        Ongoing("2","AI SQL", "June 17,2023", 76, "ongoing2"),
//        Ongoing("2","Study track App", "July 31,2023", 23, "ongoing3"),
//        Ongoing("3","OCR", "June 20,2023", 84, "ongoing4")
//    )
    fun addTask(task: Ongoing) {
        val taskRef = database.push()
        taskRef.setValue(task)
    }
    fun getTasks(listener: ValueEventListener) {
        database.addValueEventListener(listener)
    }
    fun updateTask(taskId: String, updatedTask: Ongoing) {
        database.child(taskId).setValue(updatedTask)
    }

    fun deleteTask(taskId: String) {
        database.child(taskId).removeValue()
    }


}