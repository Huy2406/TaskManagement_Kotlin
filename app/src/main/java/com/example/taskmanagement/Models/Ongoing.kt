package com.example.taskmanagement.Models

import com.google.firebase.firestore.PropertyName

data class Ongoing(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id : String = "",

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: String = "",

    @get:PropertyName("percent")
    @set:PropertyName("percent")
    var percent: Int = 0,

    @get:PropertyName("picPath")
    @set:PropertyName("picPath")
    var picPath: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description : String = "",

    @get:PropertyName("tasks")
    @set:PropertyName("tasks")
    var tasks : List<Task> = emptyList()
)
