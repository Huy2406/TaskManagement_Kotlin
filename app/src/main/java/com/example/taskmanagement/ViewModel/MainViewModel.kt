package com.example.taskmanagement.ViewModel

import androidx.lifecycle.ViewModel
import com.example.taskmanagement.Repository.MainRepository

class MainViewModel(val repository: MainRepository) : ViewModel() {
    constructor() : this(MainRepository())

//    fun loadData() = repository.items
}