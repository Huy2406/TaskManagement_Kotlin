package com.example.taskmanagement.Repository

import com.example.taskmanagement.Domain.TeamDomain

class ProfileRepository {
    val myteamItems: MutableList<TeamDomain> = mutableListOf(
        TeamDomain("Chatbot", "Project in Progress"),
        TeamDomain("AI SQL", "Complete"),
        TeamDomain("Study track App", "Project in Progress"),
        TeamDomain("OCR", "Complete")
    )

    val archiveItems: MutableList<String> = mutableListOf(
        "Prompt Translate to SQP",
        "Image Auto Download",
        "Lychees Detector"
    )
}