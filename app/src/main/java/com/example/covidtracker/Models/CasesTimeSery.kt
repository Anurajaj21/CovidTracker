package com.example.covidtracker.Models

data class CasesTimeSery(
    val dailyconfirmed: String,
    val dailydeceased: String,
    val dailyrecovered: String,
    val date: String,
    val dateymd: String,
    val totalconfirmed: String,
    val totaldeceased: String,
    val totalrecovered: String
)