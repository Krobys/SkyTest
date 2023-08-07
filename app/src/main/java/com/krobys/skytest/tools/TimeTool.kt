package com.krobys.skytest.tools

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeTool {
    fun formatPostDate(postDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val postInstant = LocalDateTime.parse(postDate, formatter).atZone(ZoneId.systemDefault()).toInstant()

        val now = Instant.now()

        val period = Period.between(postInstant.atZone(ZoneId.systemDefault()).toLocalDate(), now.atZone(ZoneId.systemDefault()).toLocalDate())
        val duration = Duration.between(postInstant, now)

        return when {
            period.years > 0 -> "${period.years} years ago"
            period.months > 0 -> "${period.months} months ago"
            period.days > 0 -> when (period.days) {
                1 -> "yesterday"
                else -> "${period.days} days ago"
            }
            duration.toHours() > 0 -> "${duration.toHours()}h ago"
            duration.toMinutes() > 0 -> "${duration.toMinutes()}m ago"
            else -> "just now"
        }
    }
}