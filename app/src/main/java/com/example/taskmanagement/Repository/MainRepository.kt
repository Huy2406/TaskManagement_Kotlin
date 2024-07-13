package com.example.taskmanagement.Repository

import com.example.taskmanagement.Domain.OngoingDomain

class MainRepository {
    val items= listOf(
        OngoingDomain("Chatbot", "July 13,2024", 50, "ongoing1"),
        OngoingDomain("AI SQL", "June 17,2023", 76, "ongoing2"),
        OngoingDomain("Study track App", "July 31,2023", 23, "ongoing3"),
        OngoingDomain("OCR", "June 20,2023", 84, "ongoing4")
    )
}