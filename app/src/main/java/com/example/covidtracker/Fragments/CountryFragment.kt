package com.example.covidtracker.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.covidtracker.Models.World.countryDataItem
import com.example.covidtracker.R
import com.example.covidtracker.Utils.InternetCheck
import kotlinx.android.synthetic.main.each_country.view.*
import kotlinx.android.synthetic.main.fragment_country.*
import kotlinx.android.synthetic.main.fragment_state.*
import kotlinx.android.synthetic.main.fragment_world.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.PieModel
import java.lang.Exception


class CountryFragment(private val unit: countryDataItem) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_country, container, false)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.country_refresh)
        val retry = view.findViewById<Button>(R.id.ct_retry)

        fetchAllData()

        retry.setOnClickListener {
            InternetCheck{
                if (it){
                    ct_no_internet.visibility = View.GONE
                    country.visibility = View.VISIBLE
                    fetchAllData()
                }else{
                    Toast.makeText(requireContext(),"No internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresh.setOnRefreshListener {
            InternetCheck{
                if (it){
                    ct_no_internet.visibility = View.GONE
                    country.visibility = View.VISIBLE
                    fetchAllData()
                }else{
                    country.visibility = View.GONE
                    ct_no_internet.visibility = View.VISIBLE
                }
            }
            refresh.isRefreshing = false
        }

        return view
    }

    private fun fetchAllData(){
        GlobalScope.launch ( Dispatchers.Main ){
            try {
                ct_name.text = unit.country
                val url = unit.countryInfo.flag
                Glide.with(this@CountryFragment)
                        .load(url)
                        .placeholder(R.color.dark_gray)
                        .into(ct_flag)

                setData()
                setPiechart()

            }catch (e: Exception){
                Log.d("country info", e.message!!)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        ct_active_cases.text = unit.active.toString()
        ct_confirmed_cases.text = unit.cases.toString()
        ct_delta_confirmed.text = "+" + unit.todayCases.toString()
        ct_recovered_cases.text = unit.recovered.toString()
        ct_delta_recovered.text = "+" + unit.todayRecovered.toString()
        ct_death_cases.text = unit.deaths.toString()
        ct_delta_deaths.text = "+" + unit.todayDeaths.toString()
        ct_critical_cases.text = unit.critical.toString()
        ct_tests.text = unit.tests.toString()
    }
    private fun setPiechart(){
        ct_piechart.clearChart()

        ct_piechart.addPieSlice(PieModel("Active", Integer.parseInt(unit.active.toString()).toFloat(), Color.parseColor("#85601F")))
        ct_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(unit.cases.toString()).toFloat(), Color.parseColor("#6568EE")))
        ct_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(unit.recovered.toString()).toFloat(), Color.parseColor("#50A754")))
        ct_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(unit.deaths.toString()).toFloat(), Color.parseColor("#F30505")))

        ct_piechart.startAnimation()
    }

}