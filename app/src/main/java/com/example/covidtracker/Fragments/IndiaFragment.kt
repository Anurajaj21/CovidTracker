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
import android.widget.Adapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Adapters.StatesAdapter
import com.example.covidtracker.Models.DataResponse
import com.example.covidtracker.Models.Statewise
import com.example.covidtracker.Models.Tested
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.ApiInterface
import com.example.covidtracker.R
import com.example.covidtracker.Utils.LoadingUtils
import kotlinx.android.synthetic.main.fragment_district.*
import kotlinx.android.synthetic.main.fragment_india.*
import kotlinx.android.synthetic.main.fragment_world.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.eazegraph.lib.models.PieModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class IndiaFragment : Fragment() {

    lateinit var adapter: RecyclerView.Adapter<StatesAdapter.StatesViewHolder>
    var list = ArrayList<Statewise>()
    var tests = ArrayList<Tested>()
    private val indiaClient = ApiClient("https://api.covid19india.org/")
    private val response: MutableLiveData<Response<DataResponse>> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_india, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.states_rv)

        val apiInterface = indiaClient.getApiClient()?.create(ApiInterface::class.java)
        Log.d("indiaa", apiInterface.toString())

        GlobalScope.launch ( Dispatchers.Main ){
            LoadingUtils.showDialog(requireContext(), true)
            response.value = apiInterface?.StateResponse()
            LoadingUtils.hideDialog()
        }

        fetchData()

        adapter = StatesAdapter(list, tests)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }


    @SuppressLint("SetTextI18n")
    private fun fetchData() {

        response.observe(viewLifecycleOwner, { response->
            if (response.code() == 200) {
                Log.d("state data", response.body().toString())
                list.addAll(response.body()?.statewise as ArrayList<Statewise>)
                val ind = list[0]
                list.removeAt(0)
                tests.addAll(response.body()?.tested as ArrayList<Tested>)
                val ind_test = tests[0]
                tests.removeAt(0)
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
                in_delta_tests.text = ind_test.totalsamplestested
                in_migrated.text = ind.migratedother
                if(ind.statenotes == ""){
                    india_note.text = "-----"
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