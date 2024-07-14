package com.example.taskmanagement.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanagement.Adapter.OngoingAdapter
import com.example.taskmanagement.R
import com.example.taskmanagement.ViewModel.MainViewModel
import com.example.taskmanagement.databinding.ActivityMainBinding
import com.example.taskmanagement.ui.theme.TaskManagementTheme

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            val ongoingAdapter by lazy { OngoingAdapter(mainViewModel.loadData()) }

            viewOngoing.apply {
                adapter=ongoingAdapter
                layoutManager = GridLayoutManager(this@DashboardActivity,2)
            }
        }
    }
}