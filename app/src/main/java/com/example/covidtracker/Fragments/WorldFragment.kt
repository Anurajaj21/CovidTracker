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
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.covidtracker.Adapters.CountriesAdapter
import com.example.covidtracker.Models.World.countryData
import com.example.covidtracker.Models.World.countryDataItem
import com.example.covidtracker.Models.World.worldData
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.ApiInterface
import com.example.covidtracker.R
import com.example.covidtracker.Utils.InternetCheck
import com.example.covidtracker.Utils.LoadingUtils
import kotlinx.android.synthetic.main.fragment_world.*
import kotlinx.coroutines.*
import org.eazegraph.lib.models.PieModel
import retrofit2.*

class WorldFragment : Fragment() {

    lateinit var adapter: RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>
    var list = ArrayList<countryDataItem>()
    private val WorldClient = ApiClient("https://corona.lmao.ninja/v2/")
    private val worldResponse: MutableLiveData<Response<worldData>> = MutableLiveData()
    private val countryResponse: MutableLiveData<Response<countryData>> = MutableLiveData()
    private lateinit var searchCountry: SearchView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_world, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.country_rv)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.wo_refresh)
        val retry = view.findViewById<Button>(R.id.wo_retry)

        searchCountry = view.findViewById(R.id.search_country)

        fetchAllData()

        retry.setOnClickListener {
            InternetCheck {
                if (it) {
                    wo_no_internet.visibility = View.GONE
                    world_frg.visibility = View.VISIBLE
                    fetchAllData()
                } else {
                    Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresh.setOnRefreshListener {
            InternetCheck {
                if (it) {
                    wo_no_internet.visibility = View.GONE
                    world_frg.visibility = View.VISIBLE
                    fetchAllData()
                } else {
                    world_frg.visibility = View.GONE
                    wo_no_internet.visibility = View.VISIBLE
                }
            }
            refresh.isRefreshing = false
        }

        adapter = CountriesAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        search()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchAllData() {
        val apiInterface = WorldClient.getApiClient()?.create(ApiInterface::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            LoadingUtils.showDialog(requireContext(), true)
            worldResponse.value = apiInterface?.WorldResponse()
            LoadingUtils.hideDialog()
            countryResponse.value = apiInterface?.CountryResponse()

            Log.i("world", worldResponse.value.toString())

        }
        fetchData()
    }

    private fun search() {
        searchCountry.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    (adapter as CountriesAdapter).filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                (adapter as CountriesAdapter).filter.filter(newText)
                return true
            }
        })
    }

    @SuppressLint("SetTextI18n", "FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchData() {

        worldResponse.observe(this, { response ->
            if (response.code() == 200) {
                val stcases: Int? = response.body()?.cases
                val sttodaycases: Int? = response.body()?.todayCases
                val stactive: Int? = response.body()?.active
                val stdeath: Int? = response.body()?.deaths
                val sttodaydeath: Int? = response.body()?.todayDeaths
                val strecover: Int? = response.body()?.recovered
                val sttodayrecover: Int? = response.body()?.todayRecovered
                val stcritical: Int? = response.body()?.critical
                val sttests: Long? = response.body()?.tests
                val staffectedCou: Int? = response.body()?.affectedCountries

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

                world_piechart.clearChart()
                setPiechart()

                Log.i("world data", "fetched")
            }

        })


        countryResponse.observe(this, {
            if (it.code() == 200) {
                list.addAll(it.body() as ArrayList<countryDataItem>)
                Log.d("country data", "fetch")
                adapter.notifyDataSetChanged()
            }
        })


    }

    private fun setPiechart() {
        world_piechart.addPieSlice(PieModel("Active", Integer.parseInt(wo_active_cases.text.toString()).toFloat(), Color.parseColor("#85601F")))
        world_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(wo_confirmed_cases.text.toString()).toFloat(), Color.parseColor("#6568EE")))
        world_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(wo_recovered_cases.text.toString()).toFloat(), Color.parseColor("#50A754")))
        world_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(wo_death_cases.text.toString()).toFloat(), Color.parseColor("#F30505")))

        world_piechart.startAnimation()

    }

}