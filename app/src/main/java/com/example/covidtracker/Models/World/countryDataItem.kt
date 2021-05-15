package com.example.covidtracker.Models.World

data class countryDataItem(
        val active: Int,
        val cases: Int,
        val country: String,
        val countryInfo: CountryInfo,
        val critical: Int,
        val deaths: Int,
        val recovered: Int,
        val tests: Int,
        val todayCases: Int,
        val todayDeaths: Int,
        val todayRecovered: Int,
        val updated: Long
)