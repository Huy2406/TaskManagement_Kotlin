package com.example.taskmanagement.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagement.Adapter.ArchiveAdapter
import com.example.taskmanagement.Adapter.MyTeamAdapter
import com.example.taskmanagement.R
import com.example.taskmanagement.ViewModel.ProfileViewModel
import com.example.taskmanagement.databinding.ActivityMainBinding
import com.example.taskmanagement.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    val profileViewModel:ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.apply {
            val myteamAdapter by lazy {MyTeamAdapter(profileViewModel.loadDataMyteam()) }
            viewTeam.apply {
                adapter=myteamAdapter
                layoutManager=LinearLayoutManager(
                    this@ProfileActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }

            val archiveAdapter by lazy {ArchiveAdapter(profileViewModel.loadDataArchive())}
            viewArchive.apply {
                adapter=archiveAdapter
                layoutManager=LinearLayoutManager(
                    this@ProfileActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }
}