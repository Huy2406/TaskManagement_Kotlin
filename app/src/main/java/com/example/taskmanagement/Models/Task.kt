package com.example.taskmanagement.Models

import com.google.firebase.database.PropertyName

data class Task(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("done")
    @set:PropertyName("done")
    var done: Boolean = false
)
