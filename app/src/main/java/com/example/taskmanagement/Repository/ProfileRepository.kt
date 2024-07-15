package com.example.taskmanagement.Repository

import com.example.taskmanagement.Domain.TeamDomain

class ProfileRepository {
    val myteamItem: MutableList<TeamDomain> = mutableListOf(
        TeamDomain("Chatbot", "Project in Progress"),
        TeamDomain("AI SQL", "Complete"),
        TeamDomain("Study track App", "Project in Progress"),
        TeamDomain("OCR", "Complete")
    )

    val archiveItem: MutableList<String> = mutableListOf(
        "UI/UX ScreenShot",
        "Kotlin Source Code",
        "Source Codes"
    )
}