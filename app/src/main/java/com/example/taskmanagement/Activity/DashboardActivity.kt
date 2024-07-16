package com.example.taskmanagement.Activity


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taskmanagement.Adapter.OngoingAdapter
import com.example.taskmanagement.Adapter.OngoingDialogUtils
import com.example.taskmanagement.Models.Ongoing
import com.example.taskmanagement.ViewModel.OnGoingViewModel
import com.example.taskmanagement.databinding.ActivityMainBinding


class DashboardActivity : AppCompatActivity() {
    private val ongoingDialogUtils = OngoingDialogUtils()

    lateinit var binding: ActivityMainBinding
    private val onGoingViewModel: OnGoingViewModel by viewModels{
        ViewModelProvider.NewInstanceFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupListeners()

        binding.textViewRefresh.setOnClickListener{
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        val ongoingAdapter = OngoingAdapter { ongoing ->
            val intent = Intent(this@DashboardActivity, OnGoingActivity::class.java)
            intent.putExtra("ongoing_id", ongoing.id)
            startActivity(intent)
        }
        binding.viewOngoing.apply {
            adapter = ongoingAdapter
            layoutManager = GridLayoutManager(this@DashboardActivity, 2)
        }
        onGoingViewModel.fetchOngoingTasks()

        // Observe ongoing tasks LiveData from ViewModel
        onGoingViewModel.ongoingTasksLiveData.observe(this, Observer { ongoingTasks ->
            ongoingAdapter.setData(ongoingTasks)
            ongoingAdapter.notifyDataSetChanged()
        })
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                // Call a function or perform an action with the searchText here
                onGoingViewModel.searchByTitle(searchText);
                ongoingAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed in this case
            }
        })


        // Fetch ongoing tasks initially
    }

    private fun setupListeners() {
        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnAdd.setOnClickListener {
            ongoingDialogUtils.showDialogAddOngoing (view = binding.root, onAddOngoing = {ongoing ->
                addOngoing(ongoing)
            })
        }
    }
    private fun addOngoing(ongoing : Ongoing) {
        onGoingViewModel.add(
            Ongoing("", ongoing.title, ongoing.date, 0,ongoing.picPath, ongoing.description, emptyList()),
            onComplete = {
                Toast.makeText(this@DashboardActivity, "Ongoing task added successfully", Toast.LENGTH_SHORT).show()
                // Data refresh is handled automatically by observing LiveData
            },
            onError = {
                Toast.makeText(this@DashboardActivity, "Failed to add ongoing task: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
