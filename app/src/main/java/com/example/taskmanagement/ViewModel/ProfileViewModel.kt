package com.example.taskmanagement.ViewModel

import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.example.taskmanagement.Repository.ProfileRepository

class ProfileViewModel(val repository: ProfileRepository):ViewModel() {
    constructor(): this(ProfileRepository())

    fun loadDataMyteam()=repository.myteamItems
    fun loadDataArchive()=repository.archiveItems
}