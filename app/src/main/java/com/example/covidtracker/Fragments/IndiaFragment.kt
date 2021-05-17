package com.example.covidtracker.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.covidtracker.Adapters.CountriesAdapter
import com.example.covidtracker.Adapters.StatesAdapter
import com.example.covidtracker.Models.India.StateResponse
import com.example.covidtracker.Models.India.Statewise
import com.example.covidtracker.Models.India.Tested
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.ApiInterface
import com.example.covidtracker.R
import com.example.covidtracker.Utils.InternetCheck
import com.example.covidtracker.Utils.LoadingUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_india.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.PieModel
import retrofit2.Response


class IndiaFragment : Fragment() {

    lateinit var adapter: RecyclerView.Adapter<StatesAdapter.StatesViewHolder>
    var list = ArrayList<Statewise>()
    var tests = ArrayList<Tested>()
    private val indiaClient = ApiClient("https://api.covid19india.org/")
    private val response: MutableLiveData<Response<StateResponse>> = MutableLiveData()
    private lateinit var searchState : SearchView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_india, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.states_rv)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.india_refresh)
        val retry = view.findViewById<Button>(R.id.in_retry)

        searchState = view.findViewById(R.id.search_state)

        InternetCheck{
            if(it){
                in_no_internet.visibility = View.GONE
                india.visibility = View.VISIBLE
                fetchAllData()
            }
            else{
                india.visibility = View.GONE
                in_no_internet.visibility = View.VISIBLE
            }
        }


        retry.setOnClickListener {
            InternetCheck{
                if (it){
                    in_no_internet.visibility = View.GONE
                    india.visibility = View.VISIBLE
                    fetchAllData()
                }else{
                    Toast.makeText(requireContext(),"No internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresh.setOnRefreshListener {
            InternetCheck{
                if (it){
                    in_no_internet.visibility = View.GONE
                    india.visibility = View.VISIBLE
                    fetchAllData()
                }else{
                    india.visibility = View.GONE
                    in_no_internet.visibility = View.VISIBLE
                }
            }
            refresh.isRefreshing = false
        }



        adapter = StatesAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        search()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchAllData(){
        val apiInterface = indiaClient.getApiClient()?.create(ApiInterface::class.java)
        Log.d("indiaa", apiInterface.toString())

        GlobalScope.launch ( Dispatchers.Main ){
            LoadingUtils.showDialog(requireContext(), true)
            response.value = apiInterface?.StateResponse()
            fetchData()
            LoadingUtils.hideDialog()
        }
    }

    private fun search() {
        searchState.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    (adapter as StatesAdapter).filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show()
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                (adapter as StatesAdapter).filter.filter(newText)
                return true
            }
        })
    }


    @SuppressLint("SetTextI18n", "FragmentLiveDataObserve")
    private fun fetchData() {

        response.observe(this, { response->
            if (response.code() == 200) {

                Log.d("state data", response.body().toString())
                list.removeAll(response.body()?.statewise!!)
                list.addAll(response.body()?.statewise as ArrayList<Statewise>)
                val ind = list[0]
                list.removeAt(0)
                tests.removeAll(response.body()?.tested!!)
                tests.addAll(response.body()?.tested as ArrayList<Tested>)
                val ind_test = tests[tests.lastIndex]
                Log.d("state data", "fetch")
                adapter.notifyDataSetChanged()


                in_active_cases.text = ind.active
                in_confirmed_cases.text = ind.confirmed
                in_delta_confirmed.text = "+" + ind.deltaconfirmed
                in_recovered_cases.text = ind.recovered
                in_delta_recovered.text = "+" + ind.deltarecovered
                in_death_cases.text = ind.deaths
                in_delta_deaths.text = "+" + ind.deltadeaths
                india_note.text = ind.statenotes
                in_update.text = ind.lastupdatedtime
                in_tests.text = ind_test.totalsamplestested
                in_delta_tests.text = "+" + ind_test.samplereportedtoday
                in_migrated.text = ind.migratedother
                if(ind.statenotes == ""){
                    india_note.text = "- - - - - - - - - - -"
                }else{
                    india_note.text = ind.statenotes
                }
                india_piechart.clearChart()
                setPiechart()

            }

        })

    }

    private fun setPiechart(){
        india_piechart.addPieSlice(PieModel("Active", Integer.parseInt(in_active_cases.text.toString()).toFloat(), Color.parseColor("#85601F")))
        india_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(in_confirmed_cases.text.toString()).toFloat(), Color.parseColor("#6568EE")))
        india_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(in_recovered_cases.text.toString()).toFloat(), Color.parseColor("#50A754")))
        india_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(in_death_cases.text.toString()).toFloat(), Color.parseColor("#F30505")))

        india_piechart.startAnimation()

    }

}