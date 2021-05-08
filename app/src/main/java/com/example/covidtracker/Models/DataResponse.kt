package com.example.covidtracker.Models

data class DataResponse(
    val cases_time_series: List<CasesTimeSery>,
    val statewise: List<Statewise>
)