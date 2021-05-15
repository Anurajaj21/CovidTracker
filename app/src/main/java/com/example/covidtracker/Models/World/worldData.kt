package com.example.covidtracker.Models.World

data class worldData(
    val active: Int,
    val affectedCountries: Int,
    val cases: Int,
    val critical: Int,
    val deaths: Int,
    val population: Long,
    val recovered: Int,
    val tests: Long,
    val todayCases: Int,
    val todayDeaths: Int,
    val todayRecovered: Int,
    val updated: Long
)