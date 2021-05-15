package com.example.covidtracker.Models.India

data class Statewise(
        var active: String,
        val confirmed: String,
        val deaths: String,
        val deltaconfirmed: String,
        val deltadeaths: String,
        val deltarecovered: String,
        val lastupdatedtime: String,
        val migratedother: String,
        val recovered: String,
        val state: String,
        val statecode: String,
        val statenotes: String
)