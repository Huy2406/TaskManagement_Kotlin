package com.example.taskmanagement.Adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.taskmanagement.Models.Ongoing
import com.example.taskmanagement.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OngoingDialogUtils {

      fun showDialogEditOngoing(view: View,onEditOngoing : (Ongoing) -> Unit,ongoing: Ongoing) {
        val builder = AlertDialog.Builder(view.context)
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_ongoing, null)

        val editTitle = dialogLayout.findViewById<EditText>(R.id.editTextTitle)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
        val datePickerIcon = dialogLayout.findViewById<ImageView>(R.id.datePickerIcon)
        val editDate = dialogLayout.findViewById<EditText>(R.id.editTextDate)
        val genreSpinner = dialogLayout.findViewById<Spinner>(R.id.genreSpinner)

        // Setup genre spinner
        setupGenreSpinner(view, genreSpinner)

        // Date picker icon click listener
        datePickerIcon.setOnClickListener {
            showDatePicker(view, editDate)
        }
        editTitle.setText( ongoing.title)
          editDate.setText(ongoing.date)
          editDescription.setText(ongoing.description)
          val selectedItem = ongoing.picPath.uppercase()

          val adapter = genreSpinner.adapter

          for (index in 0 until adapter.count) {
              val item = adapter.getItem(index) as String
              if (item.uppercase() == selectedItem) {
                  genreSpinner.setSelection(index)
                  break
              }
          }
          builder.setView(dialogLayout)
            .setPositiveButton("UPDATE") { dialogInterface, _ ->
                val title = editTitle.text.toString().trim()
                val description = editDescription.text.toString().trim()
                val date = editDate.text.toString().trim()
                val genre = genreSpinner.selectedItem.toString().trim()

                // Validate input if necessary
                if (title.isEmpty() || description.isEmpty() || date.isEmpty() || genre.isEmpty()) {
                    Toast.makeText(view.context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    onEditOngoing(Ongoing(ongoing.id,title,date, ongoing.percent,genre ,description))
                    dialogInterface.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }

      fun showDialogAddOngoing(view: View,onAddOngoing: (Ongoing) -> Unit) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(view.context)
        val inflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_ongoing, null)

        val editTitle = dialogLayout.findViewById<EditText>(R.id.editTextTitle)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editTextDescription)
        val datePickerIcon = dialogLayout.findViewById<ImageView>(R.id.datePickerIcon) // Assuming it's an ImageView for date picker
        val editDate = dialogLayout.findViewById<EditText>(R.id.editTextDate)
        val genreSpinner = dialogLayout.findViewById<Spinner>(R.id.genreSpinner)

        // Setup genre spinner
        setupGenreSpinner(view,genreSpinner)

        // Date picker icon click listener
        datePickerIcon.setOnClickListener {
            showDatePicker(view,editDate)
        }

        builder.setView(dialogLayout)
            .setPositiveButton("ADD") { dialogInterface, _ ->
                val title = editTitle.text.toString().trim()
                val description = editDescription.text.toString().trim()
                val date = editDate.text.toString().trim()
                val genre = genreSpinner.selectedItem.toString().trim()

                // Validate input if necessary
                if (title.isEmpty() || description.isEmpty() || date.isEmpty() || genre.isEmpty()) {
                    Toast.makeText(view.context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    // Call method to add ongoing with validated inputs
                    onAddOngoing(Ongoing("",title,date,0, genre ,description))
                    dialogInterface.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }

    private fun showDatePicker(view: View, editDate: EditText) {
        val context = view.context
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)

                // Format the selected date
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(selectedDate.time)

                // Set the formatted date into editTextDate
                editDate.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun setupGenreSpinner(view: View, genreSpinner: Spinner) {
        val context = view.context
        val genres = arrayOf("AI_SQL", "ORC", "APP_TRACK", "CHAT_BOX")

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            genres
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = adapter
    }
}
