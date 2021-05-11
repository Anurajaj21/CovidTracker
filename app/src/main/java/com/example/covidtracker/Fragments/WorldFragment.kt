package com.example.covidtracker.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Adapters.CountriesAdapter
import com.example.covidtracker.Models.countryDataItem
import com.example.covidtracker.Models.worldData
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.WorldApiInterface
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.fragment_world.*
import org.eazegraph.lib.models.PieModel
import retrofit2.*

class WorldFragment : Fragment() {

    lateinit var adapter: RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>
    val list = ArrayList<countryDataItem>()
    val WorldClient = ApiClient("https://corona.lmao.ninja/v2/")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_world, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.country_rv)
        apiCall()
        Log.i("check","working")
        val country = countryDataItem(56898,576889,"Afganistan",768,7687,76868,6868877,68688,66886,6677,"data:image/png;base64,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",68688)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        list.add(country)
        adapter = CountriesAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun apiCall() {

        val worldInterface = WorldClient.getApiClient()?.create(WorldApiInterface::class.java)

        val worldCall = worldInterface?.WorldResponse()

        worldCall?.enqueue(object : Callback<worldData>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<worldData>, response: Response<worldData>) {
                if (response.code() == 200){
                    val stcases : Int? = response.body()?.cases
                    val sttodaycases : Int? = response.body()?.todayCases
                    val stactive : Int? = response.body()?.active
                    val stdeath : Int? = response.body()?.deaths
                    val sttodaydeath : Int? = response.body()?.todayDeaths
                    val strecover : Int? = response.body()?.recovered
                    val sttodayrecover : Int? = response.body()?.todayRecovered
                    val stcritical : Int? = response.body()?.critical
                    val sttests : Long? = response.body()?.tests
                    val staffectedCou : Int? = response.body()?.affectedCountries

                    wo_confirmed_cases.text = stcases.toString()
                    wo_delta_confirmed.text = "+" + sttodaycases.toString()
                    wo_active_cases.text = stactive.toString()
                    wo_recovered_cases.text = strecover.toString()
                    wo_delta_recovered.text = "+" + sttodayrecover.toString()
                    wo_critical_cases.text = stcritical.toString()
                    wo_death_cases.text = stdeath.toString()
                    wo_delta_deaths.text = "+" + sttodaydeath.toString()
                    wo_tests.text = sttests.toString()
                    affected_countries.text = staffectedCou.toString()

                    world_piechart.addPieSlice(PieModel("Active", Integer.parseInt(stactive.toString()).toFloat(), Color.parseColor("#DE57D2")))
                    world_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(stcases.toString()).toFloat(), Color.parseColor("#6568EE")))
                    world_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(strecover.toString()).toFloat(), Color.parseColor("#50A754")))
                    world_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(stdeath.toString()).toFloat(), Color.parseColor("#F30505")))

                    world_piechart.startAnimation()
                }
            }

            override fun onFailure(call: Call<worldData>, t: Throwable) {
                Log.i("fetch", "fail")
            }

        })
    }

}