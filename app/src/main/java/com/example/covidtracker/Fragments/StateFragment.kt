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
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.covidtracker.Adapters.CountriesAdapter
import com.example.covidtracker.Adapters.DistrictAdapter
import com.example.covidtracker.Models.District.DistrictData
import com.example.covidtracker.Models.District.DistrictResponse
import com.example.covidtracker.Models.India.Statewise
import com.example.covidtracker.Models.India.Tested
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.ApiInterface
import com.example.covidtracker.R
import com.example.covidtracker.Utils.InternetCheck
import com.example.covidtracker.Utils.LoadingUtils
import kotlinx.android.synthetic.main.fragment_india.*
import kotlinx.android.synthetic.main.fragment_state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.PieModel
import retrofit2.Response


class StateFragment(private val unit1: Statewise, private  val unit2: Tested) : Fragment() {

    lateinit var adapter : RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>
    var list = ArrayList<DistrictData>()
    val stateClient = ApiClient("https://api.covid19india.org/v2/")
    val response : MutableLiveData<Response<DistrictResponse>> = MutableLiveData()
    private lateinit var searchDistrict : SearchView

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_state, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.district_rv)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.state_refresh)
        val retry = view.findViewById<Button>(R.id.st_retry)

        searchDistrict = view.findViewById(R.id.search_district)

        fetchAllData(view)

        retry.setOnClickListener {
            InternetCheck{
                if (it){
                    st_no_internet.visibility = View.GONE
                    state.visibility = View.VISIBLE
                    fetchAllData(view)
                }else{
                    Toast.makeText(requireContext(),"No internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresh.setOnRefreshListener {
            InternetCheck{
                if (it){
                    st_no_internet.visibility = View.GONE
                    state.visibility = View.VISIBLE
                    fetchAllData(view)
                }else{
                    state.visibility = View.GONE
                    st_no_internet.visibility = View.VISIBLE
                }
            }
            refresh.isRefreshing = false
        }

        adapter = DistrictAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        search()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @ExperimentalStdlibApi
    private fun fetchAllData(view: View){
        val apiInterface = stateClient.getApiClient()?.create(ApiInterface::class.java)
        GlobalScope.launch(Dispatchers.Main){
            LoadingUtils.showDialog(requireContext(), true)
            response.value = apiInterface?.DistrictResponse()
            LoadingUtils.hideDialog()
            Log.d("district data", response.value?.body().toString())
            setData(view)
            state_piechart.clearChart()
            setPiechart()
        }
    }

    private fun search() {
        searchDistrict.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    (adapter as DistrictAdapter).filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show()
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                (adapter as DistrictAdapter).filter.filter(newText)
                return true
            }
        })
    }

    private fun setPiechart(){
        state_piechart.addPieSlice(PieModel("Active", Integer.parseInt(st_active_cases.text.toString()).toFloat(), Color.parseColor("#85601F")))
        state_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(st_confirmed_cases.text.toString()).toFloat(), Color.parseColor("#6568EE")))
        state_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(st_recovered_cases.text.toString()).toFloat(), Color.parseColor("#50A754")))
        state_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(st_death_cases.text.toString()).toFloat(), Color.parseColor("#F30505")))

        state_piechart.startAnimation()

    }

    @ExperimentalStdlibApi
    @SuppressLint("SetTextI18n")
    private fun setData(view :  View){
        val name = view.findViewById<TextView>(R.id.st_name)
        val active = view.findViewById<TextView>(R.id.st_active_cases)
        val confirmed = view.findViewById<TextView>(R.id.st_confirmed_cases)
        val deltaConfirmed = view.findViewById<TextView>(R.id.st_delta_confirmed)
        val recovered = view.findViewById<TextView>(R.id.st_recovered_cases)
        val deltaRecovered = view.findViewById<TextView>(R.id.st_delta_recovered)
        val deaths = view.findViewById<TextView>(R.id.st_death_cases)
        val deltaDeaths = view.findViewById<TextView>(R.id.st_delta_deaths)
        val note = view.findViewById<TextView>(R.id.state_note)
        val update = view.findViewById<TextView>(R.id.st_update)
        val tests = view.findViewById<TextView>(R.id.st_tests)
        val deltaTests = view.findViewById<TextView>(R.id.st_delta_tests)
        val migrated = view.findViewById<TextView>(R.id.st_migrated)

        name.text = unit1.state
        active.text = unit1.active
        confirmed.text = unit1.confirmed
        deltaConfirmed.text = "+" + unit1.deltaconfirmed
        recovered.text = unit1.recovered
        deltaRecovered.text = "+" + unit1.deltarecovered
        deaths.text = unit1.deaths
        deltaDeaths.text = "+" + unit1.deltadeaths
        note.text = unit1.statenotes
        update.text = unit1.lastupdatedtime
        tests.text = unit2.totalsamplestested
        deltaTests.text = "+" + unit2.totalsamplestested
        migrated.text = unit1.migratedother

        
        response.observe(viewLifecycleOwner, { response->
            if (response.code() == 200){
                for (i in response.body()!!){
//                    Log.d("district", i.toString())
                    if(unit1.state.lowercase() == i.state.lowercase()){
                        Log.d("district", i.state)
                        list.addAll(i.districtData)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

}